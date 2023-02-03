# Online shop API 
Small online shop API with a shopping cart feature and user role system.

## Used technology

+ Spring(Boot, Data, Security)
+ JPA
+ MySQL
+ Maven
+ Lombok

## About this project
This API was developed using Java programming language, Spring Framework and MySQL RDBMS.
It features a user role system: The user can be either a Buyer, a Vendor, or a Manager.

+ #### Buyer:

This is the default user role that is assigned to every newly registered user. Such users are allowed to preview and modify their profile info, preview products, order products, and cancel or confirm their orders.

+ #### Vendor:

Users with the Vendor role have the same abilities as the Buyer role and are also allowed to add new products and increase/decrease available product quantity. This role can only be assigned either by a user with a Manager role or through the database.

+ #### Manager:

The Manager role provides the same abilities as the Buyer role, but it also allows to preview other users' info, change user roles, and preview orders(confirmed or in progress). Role is obtained the same way as the Vendor role.

## Orders

Order entity is the implementation of the shopping cart system. Confirmed orders cannot be changed by any of the users, while unconfirmed act as a shopping cart and can be changed at any given moment. Order can be confirmed only if there's enough of the chosen product. During ordering, the quantity of the ordered product must be specified.

## API paths
### Auth
+ **POST** `/auth/authenticate` - generates JSON web token if given credentials match the existing user.
### User
+ **POST** `/user/register` - takes user info as JSON object, creates user if given proper values(age > 14, username contains letters, numbers and '_' symbol etc.).
+ **GET** `/user`  - takes request parameter 'username', returns info of the specified profile if Bearer token matches chosen user or the requester role is Manager.
+ **DELETE** `/user` - takes request parameter 'username', deletes specified user profile if Bearer token matches chosen user.
+ **PUT** `/user` - takes request parameter 'username' and user info as a JSON object, changes user info if info contains proper values.
### Product
+ **POST** `/product` - takes product info as a JSON object, creates product if given proper values, and if the token owner has a Vendor role.
+ **DELETE** `/product/{id}` - takes product id as the path variable, and marks chosen product as 'unavailable', meaning it cannot be ordered by users or seen in the product list. Can still be visible to users with the Manager role while reviewing orders.
+ **GET** `/product/{id}` - takes product id as the path variable, returns info of the specified product.
+ **GET** `/product/products` -  takes request parameters 'page'(index of the viewed page) and 'count'(number of items shown on one page), returns a list of products.
+ **PUT** `/product/{id}` - takes product id as the path variable and quantity delta as the JSON object field 'changeQuantityBy', increases/decreases product quantity if the token owner is the same user as the product owner.
### Order
+ **GET** `/order/orderProduct/{id}` - takes product id as the path variable and product quantity as the request parameter and adds a chosen product to the current unconfirmed order of the token owner(if the unconfirmed order does not exist - creates new order).
+ **DELETE** `/order/cancel` - cancels current unfinished order of the token owner.
+ **GET** `/order/myOrder` - returns info and a list of ordered items from the current unfinished order of the token owner.
+ **GET** `/order/confirm` - confirms current unfinished order of the token owner if there's enough available products. Order becomes 'finished'.
+ **GET** `/order/{id}`  - takes order id as the path variable, returns info of the specified order if the token owner's role is Manager.
+ **GET** `/order/allOrders` - takes request parameters 'page'(index of the viewed page) and 'count'(number of items shown on one page), returns a list of orders if the token owner's role is Manager.
+ **DELETE** `/order/removeOrdered/{id}` - takes product id as the path variable, fully removes specified product from the current unfinished order of the token owner.
