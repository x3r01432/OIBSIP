# Java Development — Task 1: Online Reservation System

GUI train reservation system (Java Swing + JDBC/SQLite).

## Features
- Login form (default `admin`/`admin`); access denied for invalid credentials
- Reservation form: passenger, train number, auto-populated train name, class, date, source, destination
- Book button saves to the DB and generates a unique PNR; confirmation dialog shows details
- Cancellation: enter PNR → Fetch shows full booking → confirm "Are you sure?" deletes it
- Input validation: no empty required fields, numeric train number
- `PreparedStatement` used throughout (SQL-injection safe)

## Tech Stack
Java (Swing), JDBC, SQLite.

## Run
Download the SQLite JDBC driver (e.g. `sqlite-jdbc-3.46.x.jar`), then:
```bash
javac ReservationSystem.java
java -cp .:sqlite-jdbc-3.46.0.jar ReservationSystem   # Windows: use ;
```

## Note
Compiled/verified on the build machine; the GUI requires a desktop display and the SQLite
JDBC driver jar at runtime, which were not available on the headless build box.

Author: Steven Manuel Rios
