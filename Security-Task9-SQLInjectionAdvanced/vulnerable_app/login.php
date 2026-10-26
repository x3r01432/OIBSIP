<?php
// VULNERABLE: string-concatenated SQL. For local security-education demo only.
$db = new SQLite3(__DIR__.'/app.db');
header('Content-Type: text/html');
echo "<html><body><h2>Result</h2>";
if (isset($_GET['id'])) {
  $id = $_GET['id'];
  $sql = "SELECT first_name,last_name FROM users WHERE id = $id";
  echo "<p>Query: <code>".htmlspecialchars($sql)."</code></p>";
  $res = $db->query($sql);
  if($res){ while($row = $res->fetchArray(SQLITE3_ASSOC)){ echo "<pre>ID: ".$row['first_name']." ".$row['last_name']."</pre>"; } }
  else echo "<p>SQL error</p>";
}
if (isset($_GET['user'])) {
  $u = $_GET['user']; $p = $_GET['pass'];
  $sql = "SELECT * FROM users WHERE username = '$u' AND password = '$p'";
  echo "<p>Query: <code>".htmlspecialchars($sql)."</code></p>";
  $res = $db->query($sql); $ok=false;
  if($res){ while($row=$res->fetchArray(SQLITE3_ASSOC)){ $ok=true; echo "<pre>Welcome ".$row['first_name']."</pre>"; } }
  echo $ok ? "<h3 style='color:green'>LOGIN SUCCESS</h3>" : "<h3 style='color:red'>Login failed</h3>";
}
echo "</body></html>";
