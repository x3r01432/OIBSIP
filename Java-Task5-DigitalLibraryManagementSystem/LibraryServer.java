import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Web-based Digital Library Management System.
 * Uses only the JDK built-in HTTP server (com.sun.net.httpserver) + in-memory store,
 * so it runs with a plain JDK (no servlet container or external jars).
 * Admin and User roles; books, issue/return, overdue fines (Rs 5/day), advance booking, queries.
 */
public class LibraryServer {
    static final int FINE_PER_DAY = 5;
    static AtomicInteger bookId = new AtomicInteger(1), memberId = new AtomicInteger(1), msgId = new AtomicInteger(1);

    static class Book { int id; String title, author, isbn, category; int qty, available;
        Integer issuedTo; LocalDate due; Integer reservedBy; }
    static class Member { int id; String name, email, password; }
    static class Query { int id; String member, text; }

    static Map<Integer, Book> books = new LinkedHashMap<>();
    static Map<Integer, Member> members = new LinkedHashMap<>();
    static List<Query> queries = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        seed();
        HttpServer s = HttpServer.create(new InetSocketAddress(8080), 0);
        s.createContext("/", LibraryServer::home);
        s.createContext("/api", LibraryServer::api);
        s.setExecutor(null);
        s.start();
        System.out.println("Library server running at http://localhost:8080");
    }

    static void seed() {
        addBook("The Pragmatic Programmer", "Hunt & Thomas", "978-0135957059", "Programming", 3);
        addBook("Clean Code", "Robert C. Martin", "978-0132350884", "Programming", 2);
        addBook("Sapiens", "Yuval Noah Harari", "978-0062316097", "History", 4);
        Member m = new Member(); m.id = memberId.getAndIncrement(); m.name = "Demo User"; m.email = "user@demo.com"; m.password = "user"; members.put(m.id, m);
    }
    static void addBook(String t, String a, String isbn, String cat, int q) {
        Book b = new Book(); b.id = bookId.getAndIncrement(); b.title = t; b.author = a; b.isbn = isbn; b.category = cat; b.qty = q; b.available = q;
        books.put(b.id, b);
    }

    static void home(HttpExchange e) throws IOException {
        String html = "<!DOCTYPE html><html><head><meta charset='utf-8'><title>Digital Library</title>" +
            "<style>body{font-family:sans-serif;max-width:800px;margin:2rem auto;padding:0 1rem}" +
            "table{border-collapse:collapse;width:100%}td,th{border:1px solid #ccc;padding:6px}button{cursor:pointer}</style></head><body>" +
            "<h1>Digital Library Management System</h1>" +
            "<p>Admin: <code>admin/admin</code> &middot; User: <code>user@demo.com/user</code></p>" +
            "<p>REST API (JSON): <code>/api/books</code>, <code>/api/issue?book=1&member=1</code>, " +
            "<code>/api/return?book=1</code>, <code>/api/reserve?book=1&member=1</code>, " +
            "<code>/api/register</code>, <code>/api/login</code>, <code>/api/addbook</code>, " +
            "<code>/api/deletebook?id=1</code>, <code>/api/fine?member=1</code>, <code>/api/query</code>, <code>/api/queries</code></p>" +
            "<h2>Catalogue</h2><div id='cat'></div>" +
            "<script>fetch('/api/books').then(r=>r.json()).then(b=>{let h='<table><tr><th>ID</th><th>Title</th><th>Author</th><th>Category</th><th>Available</th></tr>';" +
            "b.forEach(x=>h+=`<tr><td>${x.id}</td><td>${x.title}</td><td>${x.author}</td><td>${x.category}</td><td>${x.available}/${x.qty}</td></tr>`);" +
            "document.getElementById('cat').innerHTML=h+'</table>';});</script></body></html>";
        send(e, 200, html, "text/html");
    }

    static void api(HttpExchange e) throws IOException {
        String path = e.getRequestURI().getPath().substring(4);
        Map<String, String> q = parse(e.getRequestURI().getQuery());
        try {
            switch (path) {
                case "/books": {
                    String cat = q.get("category"), term = q.get("search");
                    StringBuilder sb = new StringBuilder("[");
                    boolean first = true;
                    for (Book b : books.values()) {
                        if (cat != null && !b.category.equalsIgnoreCase(cat)) continue;
                        if (term != null && !(b.title.toLowerCase().contains(term.toLowerCase()) || b.author.toLowerCase().contains(term.toLowerCase()))) continue;
                        if (!first) sb.append(","); first = false;
                        sb.append(String.format("{\"id\":%d,\"title\":%s,\"author\":%s,\"category\":%s,\"qty\":%d,\"available\":%d}",
                            b.id, js(b.title), js(b.author), js(b.category), b.qty, b.available));
                    }
                    send(e, 200, sb.append("]").toString(), "application/json"); break;
                }
                case "/addbook": {
                    addBook(q.getOrDefault("title", "Untitled"), q.getOrDefault("author", "Unknown"),
                        q.getOrDefault("isbn", ""), q.getOrDefault("category", "General"), Integer.parseInt(q.getOrDefault("qty", "1")));
                    send(e, 200, "{\"msg\":\"book added\"}", "application/json"); break;
                }
                case "/deletebook": {
                    books.remove(Integer.parseInt(q.get("id")));
                    send(e, 200, "{\"msg\":\"deleted\"}", "application/json"); break;
                }
                case "/register": {
                    Member m = new Member(); m.id = memberId.getAndIncrement();
                    m.name = q.getOrDefault("name", "User"); m.email = q.get("email"); m.password = q.get("password");
                    members.put(m.id, m);
                    send(e, 200, "{\"id\":" + m.id + "}", "application/json"); break;
                }
                case "/login": {
                    if ("admin".equals(q.get("user")) && "admin".equals(q.get("password"))) { send(e, 200, "{\"role\":\"admin\"}", "application/json"); break; }
                    for (Member m : members.values()) if (m.email.equals(q.get("user")) && m.password.equals(q.get("password"))) { send(e, 200, "{\"role\":\"user\",\"id\":" + m.id + "}", "application/json"); return; }
                    send(e, 401, "{\"msg\":\"invalid\"}", "application/json"); break;
                }
                case "/issue": {
                    Book b = books.get(Integer.parseInt(q.get("book")));
                    if (b == null || b.available <= 0) { send(e, 400, "{\"msg\":\"unavailable\"}", "application/json"); break; }
                    b.available--; b.issuedTo = Integer.parseInt(q.get("member")); b.due = LocalDate.now().plusDays(14);
                    send(e, 200, "{\"msg\":\"issued\",\"due\":\"" + b.due + "\"}", "application/json"); break;
                }
                case "/return": {
                    Book b = books.get(Integer.parseInt(q.get("book")));
                    long fine = 0;
                    if (b != null && b.due != null) { long over = ChronoUnit.DAYS.between(b.due, LocalDate.now()); if (over > 0) fine = over * FINE_PER_DAY; }
                    if (b != null) { b.available++; b.issuedTo = null; b.due = null;
                        if (b.reservedBy != null) b.reservedBy = null; }
                    send(e, 200, "{\"msg\":\"returned\",\"fine\":" + fine + "}", "application/json"); break;
                }
                case "/reserve": {
                    Book b = books.get(Integer.parseInt(q.get("book")));
                    if (b == null) { send(e, 404, "{\"msg\":\"no book\"}", "application/json"); break; }
                    if (b.available > 0) { send(e, 400, "{\"msg\":\"available now, just issue it\"}", "application/json"); break; }
                    b.reservedBy = Integer.parseInt(q.get("member"));
                    send(e, 200, "{\"msg\":\"reserved\"}", "application/json"); break;
                }
                case "/fine": {
                    long total = 0;
                    int mem = Integer.parseInt(q.get("member"));
                    for (Book b : books.values()) if (b.issuedTo != null && b.issuedTo == mem && b.due != null) {
                        long over = ChronoUnit.DAYS.between(b.due, LocalDate.now()); if (over > 0) total += over * FINE_PER_DAY;
                    }
                    send(e, 200, "{\"member\":" + mem + ",\"fine\":" + total + "}", "application/json"); break;
                }
                case "/query": {
                    Query qu = new Query(); qu.id = msgId.getAndIncrement(); qu.member = q.getOrDefault("member", "anon"); qu.text = q.getOrDefault("text", "");
                    queries.add(qu);
                    send(e, 200, "{\"msg\":\"received\"}", "application/json"); break;
                }
                case "/queries": {
                    StringBuilder sb = new StringBuilder("[");
                    for (int i = 0; i < queries.size(); i++) { Query qu = queries.get(i); if (i > 0) sb.append(","); sb.append(String.format("{\"member\":%s,\"text\":%s}", js(qu.member), js(qu.text))); }
                    send(e, 200, sb.append("]").toString(), "application/json"); break;
                }
                default: send(e, 404, "{\"msg\":\"not found\"}", "application/json");
            }
        } catch (Exception ex) { send(e, 500, "{\"error\":" + js(ex.getMessage()) + "}", "application/json"); }
    }

    static String js(String s) { return s == null ? "null" : "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\""; }
    static Map<String, String> parse(String qs) {
        Map<String, String> m = new HashMap<>();
        if (qs == null) return m;
        for (String p : qs.split("&")) { String[] kv = p.split("=", 2); if (kv.length == 2) m.put(kv[0], java.net.URLDecoder.decode(kv[1], StandardCharsets.UTF_8)); }
        return m;
    }
    static void send(HttpExchange e, int code, String body, String ct) throws IOException {
        byte[] b = body.getBytes(StandardCharsets.UTF_8);
        e.getResponseHeaders().set("Content-Type", ct);
        e.sendResponseHeaders(code, b.length);
        try (OutputStream os = e.getResponseBody()) { os.write(b); }
    }
}
