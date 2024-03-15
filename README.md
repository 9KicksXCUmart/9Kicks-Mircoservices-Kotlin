# 9Kicks Mircoservices Kotlin

## 1. Set Up

### 1.1 Local Environment
Install AWS CLI and use `aws configure` command to configure your aws.

 Below are the setting for this project:
```
AWS Access Key ID: <YOUR-ACCESS-KEY>
AWS Secret Access Key: <YOUR-SECRET-ACCESS-KEY>
Default region name: ap-southeast-1
Default output format: json
```

### 1.2. Alternate Access Key and Secret Key
In case you want to use another pair of `Access Key` and `Secret Key`.
You can add the following environment variable in IntelliJ.
```
AWS_ACCESS_KEY_ID=<YOUR-ACCESS-KEY>
AWS_SECRET_ACCESS_KEY=<YOUR-SECRET-ACCESS-KEY>
```

## 2. Coding Standard

### 2.1 Camel Case
For any variable, function naming we use Camel Case
<br>e.g. `demoVar`, `demoFunc()`

### 2.2 Constructor Dependency Injection
For any dependency injection in Kotlin Spring Boot, we will use constructor dependency injection
```kotlin
class DemoController(
    private val demoController: DemoRepositoryImpl
    ) {
    fun demo(){ }
}
```
### 2.3 MVC Design Pattern
The project will be based on 4 major components

<table>
<tr>
    <td>Controller</td>
    <td>Define all API endpoints</td>
</tr>
<tr>
    <td>Service</td>
    <td>Perform all the business logic</td>
</tr>
<tr>
    <td>Repository</td>
    <td>Perform all pure CRUD</td>
</tr>
<tr>
    <td>Model</td>
    <td>Define all Class</td>
</tr>
</table>

### 2.4 CRUD Naming
For any CRUD operations, it must follow the following naming:

| Operation | Naming             | Example        |
|-----------|--------------------|----------------|
| Create    | `add<Object>()`    | `addUser()`    |
| Read      | `get<Object>()`    | `getUser()`    |
| Update    | `update<Object>`() | `updateUser()` |
| Delete    | `delete<Object>()` | `deleteUser()` |

### 2.5 `get()` and `find()`
For any function name in `Service` Layer, we will use `find()`.
e.g. `fun findUserById()`
<br>
For any function name in `Repository` Layer, we will use `get()`.
e.g. `fun getUserById()`

### 2.6 API Endpoint Naming
For any API we follow the following standard `/api/v1/<controller-name>/<resources>`
<br>e.g.`/api/v1/account-summary/user-details`
#### 2.6.1 Hyphenation
For any API we use hyphen `-` to separate words
<br>e.g.`/api/v1/account-summary/user-details`

#### 2.6.2 `s` and without `s`
For any API that retrieves only 1 resource, we use singular form to indicate
<br>e.g.`/api/v1/user/<user-id>`

For any API that retrieves more than 1 resources, we use plural form, `s`, to indicate
<br>e.g.`/api/v1/users`

### 2.7 `all` and without `all`
For any functions that retrieve 1 object only, we use singular form
<br>e.g. `getUser()`

For any functions that retrieve more than 1 object, we use plural form
<br>e.g. `getAllUsers()`


### 2.8 Kotlin Data Class
For any model class/object, we will use Kotlin Data Class to construct

Non-Nested Class
```kotlin
data class Person(
    val age: Int,
    val height: Int
)
```
Nested Class
```kotlin
data class Person(
    val age: Int,
    val detail: Detail
) {
    data class Detail(
        val height: Int,
        val weight: Int
    )
}
```

### 2.9 Data Transfer Object
For any object that we receive from remote calls (backend, frontend, database),
we create a `DTO` object for it.<br>
**Beware: It is different from the main model class `UserDTO != User`**

For example:<br>
We want to send an `User` object from `backend` to `frontend - Account Summary`.<br>
We define a `UserDTO`:
```kotlin
data class UserDTO(
    val age: Int
)
```
It encapsulates some data that `frontend - Account Summary` does not need to know, e.g. `creditCardNumber`

### 2.10 Request and Response
For any object that is received as a **response**. We use place `response` after the object name.<br>
e.g. `UserResponse`

For any object that is being sent as a **request**. We use place `request` after the object name.<br>
e.g. `UserRequest`