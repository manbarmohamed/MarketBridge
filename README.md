# 🏪 MarketBridge — Full Stack Marketplace Backend

MarketBridge is a RESTful backend application built with **Spring Boot** that powers a dynamic online marketplace. It allows users to buy, sell, search, favorite, and comment on products in categories like real estate, vehicles, electronics, fashion, and more. It includes real-time chat between buyers and sellers using **WebSockets**, and integrates with **Elasticsearch** for advanced search features.

## 🚀 Features

- 👥 Role-based user management (`Buyer`, `Seller`) with inheritance
- 🏷️ Product posting by category
- 🔍 Full-text search with Elasticsearch
- 💬 Real-time messaging via WebSockets
- 🧡 Favorites and product comments
- 🖼 Upload multiple images per product
- 📦 REST API with Swagger UI documentation
- 🐘 PostgreSQL as primary relational database
- 🔐 JWT security (optional, configurable)
- 🐳 Dockerized infrastructure
- ⚙️ CI/CD pipeline with GitHub Actions + DockerHub

## 📁 Project Structure

```
marketbridge/
├── config/                     # App and Elasticsearch config
├── user/                      # User, Buyer, Seller entities + logic
├── product/                   # Product logic and image upload
├── category/                  # Category handling
├── comment/                   # Comments system
├── favorite/                  # Favorites per user
├── message/                   # Messaging via WebSocket
├── search/                    # Elasticsearch indexing & search
├── dto/                       # Request & Response DTOs
├── resources/
│   └── application.yml
├── Dockerfile
├── docker-compose.yml
├── README.md
```

> Each feature is organized by its respective package structure, making it easier to navigate and maintain the codebase.

## ⚙️ Tech Stack

| Layer     | Technology                                |
|-----------|--------------------------------------------|
| Backend   | Spring Boot 3.4.4, Java 17                |
| DB        | PostgreSQL 15.3                          |
| Search    | Elasticsearch 8.6.2                      |
| Realtime  | Spring WebSocket                         |
| Build     | Maven                                    |
| Test      | JUnit 5, Mockito (inline agent setup)    |
| Deploy    | Docker, DockerHub, GitHub Actions        |

## 🐳 Running Locally with Docker

### Prerequisites

- Docker & Docker Compose installed

### Start Services

```bash
docker-compose down -v
docker-compose up --build
```

Services:
- 📦 App: [http://localhost:8080](http://localhost:8080)
- 🔍 Elasticsearch: [http://localhost:9201](http://localhost:9201)
- 🐘 PostgreSQL: `localhost:5432`

## 🧪 Testing

Run unit and integration tests:

```bash
./mvnw test
```

> Make sure `mockito-inline` is configured and added to the test scope.

## 🧾 Sample Requests

### ▶️ Create a Category

```http
POST /api/categories
Content-Type: application/json

{
  "name": "Vehicles",
  "description": "All types of vehicles including cars, bikes, and trucks."
}
```

### ▶️ Create a Product

```http
POST /api/products
Content-Type: application/json

{
  "name": "Used Toyota Corolla",
  "description": "Clean and well-maintained",
  "location": "Casablanca",
  "price": 8500,
  "status": "AVAILABLE",
  "categoryId": 1,
  "sellerId": 2
}
```

## 📄 API Documentation

Swagger UI is available at:  
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## ✅ Troubleshooting

| Issue                                  | Fix                                                                 |
|----------------------------------------|----------------------------------------------------------------------|
| `Connection refused` to Elasticsearch | Use `elasticsearch:9200` as host **inside Docker**, not `localhost` |
| App container restarts in loop        | Use healthchecks + `depends_on` to delay app start                  |
| Mockito inline warning                | Use `mockito-inline` and pass `-javaagent` in `maven-surefire-plugin` |

## 🪪 License

Licensed under the [MIT License](LICENSE).

## 👤 Author

Built by a passionate **Full Stack Java & Angular** developer.

- 💼 Open to freelance & full-time opportunities
- 🔗 [LinkedIn](https://www.linkedin.com)
- 📫 Contact: [email@example.com](mailto:email@example.com)

## Repository

You can find the repository at: [MarketBridge GitHub Repository](https://github.com/manbarmohamed/MarketBridge.git)
