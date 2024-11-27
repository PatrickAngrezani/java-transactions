# **Finance Core API**

## **Description**
The `Finance Core API` is an application developed to manage financial transactions, perform merchant validations and make easier the access to payment data. It uses Spring Boot, MongoDB and Mockito for tests.

## **Index**
1. [Prerequisites](#Prerequisites)
2. [Configuration and Installation](#configuration-and-installation)
3. [Project Structure](#project-structure)
4. [API routes](#api-routes)
5. [Data models](#data-models)
6. [Use examples](#use-examples)
7. [Tests](#tests)
8. [Technologies used](#technologies-used)
9. [Contribution](#contribution)
10. [License](#license)

---

## **1. Prerequisites**
Ensure you have following programs installed:
- Java 17+
- Maven 3.8+
- MongoDB
- Git

---

## **2. Configuration and Installation**

1. Clone repository:
```bash
git clone https://github.com/PatrickAngrezani/java-transactions
cd finance-core
```

2. Install dependences
```bash
mvn clean install
```

3. Configure the MongoDB database:
- Create a database called finance_core.

4. Run application:
```bash
mvn spring-boot run
```

## **3. Project Structure**
```plaintext
src/main/java/com/example/finance_core/
├── controller/        # API controllers
├── model/             # Data models
├── repository/        # MongoDB Repository
├── service/           # Business logic
├── util/              # Utilities (Validations, etc.)
src/test/java/com/example/finance_core/
├── service/           # Service tests using Mockito
```

## **4. API routes**

### Transactions
| Method | Endpoint                                                                                   | Description                                                             |
|--------|--------------------------------------------------------------------------------------------|-------------------------------------------------------------------------|
| GET    | `/api/transactions`                                                                        | Return all transactions.                                                |
| GET    | `/api/transactions/{id}`                                                                   | Return one transaction by ID.                                           |
| GET    | `/api/balances/{merchantCode}`                                                             | Return balances.                                                        |
| POST   | `/api/transactions`                                                                        | Create a new transaction.                                               |
| POST   | `/api/statement`                                                                           | Generate Statement.                                                     |
| GET    | `/api/transactions/filter?description={value}&paymentMethod={value}&cardHolderName={value}`| Filter transactions by description, payment method and card holder name.|

#### Query Parameters
- **description**    (optional): Filter transactions that match with description.
- **paymentMethod**  (optional): Filter by the payment method, as `credit` or `debit`.
- **cardHolderName** (optional): Filter by card holder.

## **5. Data models**

**Transaction**
```json
{
  "id": "string",
  "merchantCode": "string",
  "amount": "double",
  "description": "string",
  "paymentMethod": "string",
  "finalAmount": "double",
  "status": "string",
  "paymentDate": "date",
  "createdAt": "date"
}
```

**Merchant**
```json
{
  "merchantCode": "string",
  "description": "string"
}
```

## **6. Use examples**

**Create a Transaction**
```bash
curl -X POST http://localhost:8080/api/transactions \
-H "Content-Type: application/json"
-d '{
    "amount": 100.00,
    "description": "store E",
    "paymentMethod": "debit",
    "cardNumber": "0000000000001634",
    "cardHolderName": "Name Example",
    "cardExpirationDate": "10/27",
    "cvv": "745"
}'
```

**Response**
```bash
{
    "id": "d1447ec6-cc2e-4a32-8a8c-b252360e64bb",
    "merchantCode": "73fa43c7-1db4-4e39-89a4-61246b508074",
    "amount": 100.0,
    "description": "loja E",
    "paymentMethod": "debit",
    "finalAmount": 97.0,
    "status": "paid",
    "cardNumber": "**** **** **** 1634",
    "cardHolderName": "Name Example",
    "cardExpirationDate": "10/27",
    "cvv": "745",
    "paymentDate": "2024-11-27T14:36:08.643+00:00",
    "createdAt": "2024-11-27T14:36:08.635+00:00"
}
```

## **7. Tests**
Tests are written using Mockito to simulate dependences and validate behavior.

**Run tests**
```bash
mvn test
```

**Test Example**
```java
	@Test
	void shouldReturnCorrectBalances() {
		// Mocks paid transactions
		List<Transaction> paidTransactions = List.of(new Transaction("merchant1", 100.0, "paid"),
				new Transaction("merchant1", 50.0, "paid"));

		// Mocks of waiting transactions
		List<Transaction> waitingFundsTransactions = List.of(new Transaction("merchant1", 200.0, "waiting_funds"));

		// Set mock behavior
		when(transactionRepository.findByMerchantCodeAndStatus("merchant1", "paid")).thenReturn(paidTransactions);
		when(transactionRepository.findByMerchantCodeAndStatus("merchant1", "waiting_funds"))
				.thenReturn(waitingFundsTransactions);

		// Execute method
		Map<String, Double> balances = balanceService.getBalances("merchant1");

		// Verify results
		assertEquals(150.0, balances.get("available"));
		assertEquals(200, balances.get("waiting_funds"));
	}
```

## **8. Technologies Used**
- **Java:** Main language.
- **Spring Boot:** Framework to API fast development.
- **MongoDB:** Database NoSQL.
- **Maven**: Dependency manager.
- **Mockito**: Framework to mock tests.

## **9. Contribution**
Follow below steps to contribute:
1. Make project fork.
2. Create a branch to feature:
```bash
git checkout -b feat/feature-name
```
3. Commit your changes:
```bash
git commit -m "feat: New feature added"
```
4. Make a push to the branch
```bash
git push origin feat/feature-name
```
5. Open a pull request

## **10. License**
This project is under MIT license.