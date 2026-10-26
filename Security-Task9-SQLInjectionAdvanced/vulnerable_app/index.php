<?php /* Intentionally vulnerable demo app (OIBSIP Security track). LOCAL USE ONLY. */ ?>
<!doctype html><html><head><title>Vuln Bank - Login</title></head><body>
<h1>Vuln Bank Portal</h1>
<form method="GET" action="login.php">
User ID: <input name="id" value="1"> <input type="submit" value="Lookup">
</form>
<hr><form method="GET" action="login.php">
Username: <input name="user"> Password: <input name="pass" type="text"> <input type="submit" value="Login">
</form></body></html>
