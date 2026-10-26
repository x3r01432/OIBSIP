# WebDev Level 3 — Task 1: Pizza Delivery Full-Stack Application (MERN)

A full-stack pizza ordering & inventory platform with separate **User** and **Admin** roles,
custom pizza builder, Razorpay (test mode) checkout, live order tracking, automated stock
decrement, and a scheduled low-stock email alert.

## Tech Stack
- **Frontend:** React.js (Vite) + React Router + Axios (`client/`)
- **Backend:** Node.js + Express.js (`server/`)
- **Database:** MongoDB (Mongoose)
- **Payments:** Razorpay (test mode)
- **Email/Jobs:** Nodemailer + node-cron

## Feature coverage
**User side:** registration with email verification, JWT login, forgot-password (reset token),
dashboard of pizza varieties, 4-step custom builder (base → sauce → cheese → veggies),
order summary, Razorpay test checkout, real-time order status via polling (Order Received →
In Kitchen → Sent to Delivery).
**Admin side:** admin-only routes (role check), inventory dashboard, automatic stock
decrement on order, manual stock updates, order management with status changes (reflected
on the user dashboard in real time), and an hourly node-cron job emailing the admin when any
item drops below its threshold.

## Setup
```bash
# backend
cd server && npm install && cp .env.example .env   # fill in your values
npm run dev                                          # needs a running MongoDB
# seed inventory once:
curl -X POST http://localhost:5000/api/inventory/seed
# frontend
cd ../client && npm install && npm run dev
```

## Status / what is not tested on the box
This project requires a running MongoDB instance, SMTP credentials, and Razorpay test keys,
none of which are provisioned on the build machine, so it was **not run end-to-end here**.
The code is complete and structured to run once those services are configured. The
Razorpay call has a test-mode fallback so the order flow works without live keys.

Author: Steven Manuel Rios
