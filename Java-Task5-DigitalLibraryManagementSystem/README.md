# Java Development — Task 5: Digital Library Management System

A **web-based** library system with Admin and User roles. Built on the JDK's built-in
HTTP server (`com.sun.net.httpserver`) with an in-memory store, so it runs with a plain JDK —
no servlet container or external jars required.

## Features
**Admin:** admin login (`admin/admin`), add book (title/author/ISBN/category/quantity),
delete book records, view catalogue/issued state, view member queries, fine management.
**User:** registration & login, browse catalogue by category (`?category=`), search by
title/author (`?search=`), issue a book (decrements availability, 14-day due date),
return a book (increments availability, auto-calculates overdue fine at Rs 5/day),
advance booking/reserve a currently-issued book, and a contact/query form stored server-side.

## Tech Stack
Java (JDK built-in HTTP server), HTML/JS frontend (served at `/`), in-memory model.
(Checklist mentions Servlets/JSP or Spring Boot + MySQL — this implementation uses the
JDK HTTP server + in-memory store so it is self-contained and runnable without extra setup.
Swapping the store for JDBC/MySQL is a drop-in change.)

## Run
```bash
javac LibraryServer.java
java LibraryServer
# then open http://localhost:8080  and try the /api/* endpoints
```

Author: Steven Manuel Rios
