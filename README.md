
# Bitespeed-Identity-Reconciliation

An application to keep track of the contact information of the customers.


## Tech Stack
- Backend framework: Spring Boot (2.7.12)
- Database:  H2(in-memory database)
## API Reference

#### Request

```
  POST /contact/identify
```
#### Request Body

* email should be in valid format
* phoneNumber can have 6 or more digits & should not start with 0
```
{
  "email": "String",
  "phoneNumber": "String"
}
```

#### Response

```
{
  "primaryContactId": Integer,
  "emails": String[],
  "phoneNumbers": String[],
  "secondaryContactIds": String[]
}
```

## Steps to execute
**1. In Windows cmd:**
- pull docker image 

   `docker pull nehap26/bitespeed-identity:v1.0` 

- run docker image 

  `docker run -p 8080:8000 nehap26/bitespeed-identity:v1.0`

**2. Using Postman:**

- create a POST request, url:

  `http://localhost:8080/contact/identify`

- raw JSON body:

  ```
  {
    "email": "lorraine@hillvalley.edu",
    "phoneNumber": "123456"
  }
  ```
- send request

**3. Using Swagger:**

- in chrome browser, run - 

  `http://localhost:8080/contact/swagger-ui.html`

- click on 'Try it out' in the POST API, & pass the request body -

  ```
  {
    "email": "lorraine@hillvalley.edu",
    "phoneNumber": "123456"
  }
  ```
- then execute.
