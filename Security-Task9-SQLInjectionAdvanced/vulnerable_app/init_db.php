<?php
$db = new SQLite3(__DIR__.'/app.db');
$db->exec("DROP TABLE IF EXISTS users");
$db->exec("CREATE TABLE users(id INTEGER PRIMARY KEY, username TEXT, password TEXT, first_name TEXT, last_name TEXT, ssn TEXT)");
$rows = [
 ['admin','5f4dcc3b5aa765d61d8327deb882cf99','System','Administrator','411-00-1234'],
 ['gordonb','e99a18c428cb38d5f260853678922e03','Gordon','Brown','411-00-5678'],
 ['pablo','0d107d09f5bbe40cade3de5c71e9e9b7','Pablo','Picasso','411-00-9012'],
 ['smithy','5f4dcc3b5aa765d61d8327deb882cf99','Bob','Smith','411-00-3456'],
];
$i=1; foreach($rows as $r){ $db->exec("INSERT INTO users VALUES($i,'".$r[0]."','".$r[1]."','".$r[2]."','".$r[3]."','".$r[4]."')"); $i++; }
echo "seeded\n";
