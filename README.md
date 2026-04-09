# 🏨 Hotel Management System

A desktop-based **Hotel Management System** built using **JavaFX + Maven**.
This project helps manage **rooms, bookings, checkout, billing, housekeeping, feedback, logs, and dashboard analytics** using a simple file-based storage system.

---

## 📌 Overview

This application is designed as a mini hotel administration system for managing day-to-day hotel operations through a GUI.

It includes:

* Room management
* Customer booking
* Checkout and billing
* Housekeeping workflow
* Feedback collection
* Dashboard analytics with charts
* File-based persistence (`.txt` storage)
* Auto-generated text and PDF bills

---

## 🚀 Features

### 1) Login System

* Admin login screen using JavaFX
* Demo credentials included
* Redirects to dashboard on successful login

### 2) Room Management

* Add new rooms
* Edit room details
* Delete rooms (only when available)
* Search rooms by:

  * Room number
  * Room type
  * Amenities
  * Status
* View all rooms / only available rooms

### 3) Booking Management

* Book a room for a customer
* Capture:

  * Customer name
  * Contact number
  * Room number
  * Check-in date
  * Planned checkout date
  * Discount type
  * Payment method
* Automatically calculates:

  * Planned stay days
  * Original bill
  * Discount amount
  * Final bill

### 4) Discount Support

Supported discount types:

* No Discount
* Festival Discount
* Loyalty Discount
* Student Discount
* Corporate Discount
* Long Stay Discount

### 5) Checkout Management

* Checkout by booking ID
* Automatically calculates:

  * Stayed days
  * Recalculated bill
  * Discount re-evaluation
  * Late checkout fee
  * Refund (if applicable)
* Marks room as **Cleaning** after checkout
* Generates bill in:

  * `.txt`
  * `.pdf`

### 6) Housekeeping Management

* Mark room as clean after checkout
* Changes room status from:

  * `Cleaning` → `Available`
* Includes a **thread synchronization / race condition simulation** feature for demo purposes

### 7) Feedback Module

* Add customer feedback
* Store:

  * Customer name
  * Rating
  * Comments
* Used for average rating analytics

### 8) Dashboard Analytics

The dashboard displays hotel statistics such as:

* Total rooms
* Available rooms
* Occupied rooms
* Revenue
* Occupancy rate
* Active bookings
* Checked out bookings
* Average rating

### 9) Charts & Visualization

Includes:

* Occupancy Pie Chart
* Booking Status Pie Chart
* Room Type Bar Chart
* Revenue by Room Type Bar Chart
* Rating Bar Chart

### 10) Logging System

Tracks system events such as:

* Login success/failure
* Add/Edit/Delete room
* Booking creation
* Checkout
* Feedback submission

---

## 🛠 Tech Stack

* **Java 22**
* **JavaFX 21**
* **Maven**
* **FXML** for UI layout
* **CSS** for styling
* **iTextPDF** for PDF bill generation
* **File-based storage** (`.txt` files)

---

## 📂 Project Structure

```text
JAVA_HOTEL_MANAGEMENT-main/
├── README.md
└── HotelManagementSystems/
    └── hotel-management-system/
        ├── pom.xml
        ├── bills/
        │   ├── BOOK-XXXX.txt
        │   └── BOOK-XXXX.pdf
        ├── data/
        │   ├── rooms.txt
        │   ├── bookings.txt
        │   ├── feedback.txt
        │   └── logs.txt
        ├── src/
        │   └── main/
        │       ├── java/
        │       │   └── com/hotel/
        │       │       ├── MainApp.java
        │       │       ├── controller/
        │       │       │   ├── LoginController.java
        │       │       │   └── DashboardController.java
        │       │       ├── model/
        │       │       │   ├── Room.java
        │       │       │   ├── Booking.java
        │       │       │   ├── Customer.java
        │       │       │   ├── Feedback.java
        │       │       │   └── BillPreview.java
        │       │       ├── service/
        │       │       │   ├── AuthService.java
        │       │       │   ├── FileService.java
        │       │       │   ├── HotelService.java
        │       │       │   └── LogService.java
        │       │       └── util/
        │       │           ├── BillGenerator.java
        │       │           ├── DiscountUtil.java
        │       │           └── IdGenerator.java
        │       └── resources/
        │           ├── login.fxml
        │           ├── dashboard.fxml
        │           └── style.css
        └── target/
```

---

## ⚙️ How It Works

### Application Flow

1. User opens the application
2. Login screen appears
3. Admin logs in
4. Dashboard opens
5. Admin can:

   * manage rooms
   * create bookings
   * process checkouts
   * mark housekeeping status
   * view analytics
   * collect feedback

---

## 🔐 Default Login Credentials

```text
Username: admin
Password: admin123
```

---

## ▶️ How to Run the Project

## Option 1 — Run using IntelliJ IDEA (Recommended)

### Requirements

Make sure you have installed:

* **JDK 22**
* **Maven** (or IntelliJ bundled Maven)
* Internet connection for first dependency download

### Steps

1. **Extract the ZIP**
2. Open IntelliJ IDEA
3. Click **Open**
4. Select this folder:

```text
HotelManagementSystems/hotel-management-system
```

5. Wait for Maven dependencies to load
6. Make sure project SDK is set to **Java 22**
7. Open:

```text
src/main/java/com/hotel/MainApp.java
```

8. Click **Run**

---

## Option 2 — Run using Maven from Terminal

### Step 1: Open terminal inside project folder

```bash
cd HotelManagementSystems/hotel-management-system
```

### Step 2: Run the JavaFX application

```bash
mvn clean javafx:run
```

This will launch the GUI.

---

## 📦 Build the Project

To compile the project:

```bash
mvn clean compile
```

To package the project:

```bash
mvn clean package
```

Generated files will appear in:

```text
target/
```

---

## 💾 Data Storage

This project uses **plain text files** instead of a database.

### Files Used

#### `data/rooms.txt`

Stores room records.

Example:

```text
1,Single,500.0,Available,AC;WIFI,1 bed
2,Double,1000.0,Occupied,AC;WIFI;TV,2 bed
```

#### `data/bookings.txt`

Stores booking and checkout records.

#### `data/feedback.txt`

Stores customer feedback.

#### `data/logs.txt`

Stores activity logs.

---

## 🧾 Bill Generation

On successful checkout, the system automatically generates:

* **Text bill** → `bills/BOOK-ID.txt`
* **PDF bill** → `bills/BOOK-ID.pdf`

Bill includes:

* Booking ID
* Customer details
* Room details
* Stay duration
* Discount info
* Late fee
* Refund amount
* Final payable amount
* Payment method

---

## 🧠 Business Logic Summary

### Booking Logic

When a room is booked:

* Room must be `Available`
* Planned days are calculated using check-in and checkout dates
* Original bill = `pricePerDay × plannedDays`
* Discount is applied
* Final bill is stored
* Room status becomes `Occupied`

### Checkout Logic

When checkout happens:

* Actual stay duration is recalculated
* Late checkout fee may be added
* Refund is computed if the guest leaves early
* Room status becomes `Cleaning`
* Bill is generated

### Housekeeping Logic

When room is marked clean:

* Room status changes from `Cleaning` to `Available`

---

## 📊 Supported Room Status Values

* `Available`
* `Occupied`
* `Cleaning`
* `Maintenance` *(supported in model/status design)*

---

## 📈 Analytics Available

The dashboard provides insight into:

* Occupancy trends
* Booking status distribution
* Revenue by room type
* Feedback rating distribution
* Average hotel rating

---

## 🧪 Demo Data Included

This project already contains sample data in:

* `data/rooms.txt`
* `data/bookings.txt`
* `data/feedback.txt`
* `data/logs.txt`
* `bills/`

So after running, you can directly test the system without starting from scratch.

---

## ❗ Important Notes

### Java Version

This project is configured for:

```xml
<maven.compiler.source>22</maven.compiler.source>
<maven.compiler.target>22</maven.compiler.target>
```

So **Java 22 is recommended**.

If your system has Java 17 or 21 only, the project may fail unless you update the `pom.xml` accordingly.

### JavaFX Dependency

JavaFX is already configured in `pom.xml`, so using:

```bash
mvn javafx:run
```

is the easiest way to run it.

---

## 🐞 Common Issues & Fixes

### 1) `javafx runtime components are missing`

**Fix:** Run using Maven instead of plain Java:

```bash
mvn javafx:run
```

---

### 2) `Invalid target release: 22`

**Cause:** Your installed JDK is lower than Java 22.

**Fix:**

* Install JDK 22, OR
* Change `pom.xml` to match your installed Java version

Example for Java 21:

```xml
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>
```

---

### 3) PDF bills are not generating

**Fix:** Ensure Maven dependencies downloaded correctly and project has write permission to:

```text
bills/
```

---

### 4) Data is not updating

**Fix:** Check if these files exist and are writable:

```text
data/rooms.txt
data/bookings.txt
data/feedback.txt
data/logs.txt
```

---

## 🌟 Suggested Improvements (Future Scope)

You can mention these in viva / project presentation:

* Add database integration (MySQL / PostgreSQL)
* Add user roles (Admin / Receptionist / Housekeeping)
* Add room images
* Add online payment gateway integration
* Add search filters and date reports
* Add export to Excel
* Add customer check-in ID proof upload
* Add invoice email sending
* Add booking cancellation feature
* Add OTP / secure authentication

---

## 🎓 Academic Relevance

This project demonstrates:

* JavaFX GUI development
* MVC-style code organization
* OOP concepts
* File handling in Java
* Event-driven programming
* Billing logic implementation
* Java collections usage
* Basic multithreading synchronization demo

---

## 👨‍💻 Author

**Your Name Here**
Java Hotel Management System Project

*(Replace with your actual name / USN / college details if needed.)*

---

## 📜 License

This project is for **academic / learning purposes**.

---

## ✅ Quick Start

```bash
cd HotelManagementSystems/hotel-management-system
mvn clean javafx:run
```

Then login with:

```text
admin / admin123
```
