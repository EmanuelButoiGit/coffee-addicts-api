# ☕ Coffee Addicts API 🧠

## 📖 Overview

You have been hired by a company that builds an app for coffee addicts.  
Your task is to implement a **GraphQL API** that takes a user’s coordinates and returns a list of the **three closest coffee shops**, ordered from **closest to farthest**, including the **distance from the user**.

## ✅ Requirements

### ⚙️ Functionality
- Accept user coordinates (`X`, `Y`)
- Return the **three closest coffee shops**
- Include:
  - Coffee shop name
  - Location (coordinates)
  - Distance from the user
- Distances must be **rounded to 4 decimal places**
- Assume all coordinates lie on a **2D plane**
- Results must be ordered from **closest to farthest**

## 🗂️ Data Source

Coffee shop data is stored in a **remote CSV file** 
🌐 Example remote source:
https://raw.githubusercontent.com/

### ⚠️ Data Quality Considerations
- The CSV may contain **malformed or invalid entries**
- Such entries must be **handled gracefully**
- Invalid rows should be **skipped and logged**
- The CSV is fetched from a **network location**

## 📤 API Response

The API returns a list of the **three closest coffee shops**, including:
- ☕ Name
- 📌 Location (coordinates)
- 📏 Distance from the user (**rounded to 4 decimal places**)

## 🧪 Example

📍 Input coordinates:
X = 47.6
Y = -122.4

📋 Expected response:

- Starbucks Seattle2
- Starbucks Seattle
- Starbucks SF
## 🏗️ Architecture Overview

### 🧩 Core Services

- **CoffeeShopLocationService**  
  Fetches coffee shop data from CSV, validates coordinates, calculates distances, and returns the three closest coffee shops.

- **CsvDataFetcherService**  
  Retrieves CSV data from a remote URL.

- **CsvParserService**  
  Parses CSV into `CoffeeShopLocation` objects, skipping malformed lines and logging errors.

## 🔮 GraphQL

🖥️ GraphiQL UI is available at:
http://localhost:8080/graphiql?path=/graphql

## 🧪 Testing

- ✅ **Total tests:** 35
- 🧩 Covers **controllers** and **services**

### 🔍 Tested Scenarios

- GraphQL queries for fetching locations and server health
- Distance calculation and top 3 selection
- Handling missing, empty, or invalid CSV files
- Malformed lines, duplicates, headers, and extra columns
- Mixed delimiters and special symbols
- Encrypted or malicious content
- Large CSV files
- Input validation:
  - NaN
  - Infinity
  - Extreme numeric values
- All skipped entries and parsing errors are logged

## 🛠️ Prerequisites

- Java 17 (JDK) installed  
  Ensure `JDK_PATH` in the batch file points to your JDK installation.
- Network access to fetch the remote CSV
- Windows environment (for running the `.bat` file)
- A web browser to access GraphiQL

## ▶️ Running the Application

1. Install Java 17
2. Configure `JDK_PATH` in the batch file
3. Run the `.bat` file
4. Open:
http://localhost:8080/graphiql?path=/graphql
