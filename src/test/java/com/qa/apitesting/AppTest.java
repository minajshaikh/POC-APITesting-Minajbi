package com.qa.apitesting;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.Assert;

public class AppTest {
    /**
     * Test1: Verify if the API is up and running
     * URL is https://simple-grocery-store-api.glitch.me/status
     * Status code should be 200
     * Response should be "UP"
     */

    @Test
    public void VerifyAPIStatus() {
        String url = "https://simple-grocery-store-api.glitch.me/status";
        int ExpectedStatusCode = 200;
        String ExpectedResponse = "UP";
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        // Verifying the status code - 200
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        String responseString = response.jsonPath().get("status");
        // Verifying the response is UP for the status endpoint
        Assert.assertEquals(responseString, ExpectedResponse);
    }

    /**
     * Test2: Verify if the API is returning all the products
     * URL is https://simple-grocery-store-api.glitch.me/products
     * Status code should be 200
     * Response should contain all the products
     */

    @Test
    public void VerifyAllProducts() {
        String url = "https://simple-grocery-store-api.glitch.me/products";
        int ExpectedStatusCode = 200;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        // Verifying the status code - 200
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        String responseString = response.getBody().asString();
        // Verifying the response - should contain all the products
        Assert.assertTrue(responseString.contains("id"));
        Assert.assertTrue(responseString.contains("category"));
        Assert.assertTrue(responseString.contains("name"));
        Assert.assertTrue(responseString.contains("inStock"));
    }

    /**
     * Test3: Verify if the API is returning a single product
     * Product id is 4646
     * URL is https://simple-grocery-store-api.glitch.me/products/4646
     * Status code should be 200
     * Response should contain the product with id 4646
     */
    @Test
    public void VerifySingleProduct() {
        String url = "https://simple-grocery-store-api.glitch.me/products/";
        int ExpectedStatusCode = 200;
        int ExpectedProductId = 4646;
        Response response = RestAssured.get(url + ExpectedProductId);
        int statusCode = response.getStatusCode();
        // Verifying the status code - 200
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        // Verifying the response - should contain the product with id 4646
        int productId = response.jsonPath().get("id");
        Assert.assertEquals(productId, ExpectedProductId);
    }

    /**
     * Test4: Verify the status code, headers and structure of the response
     * URL is https://simple-grocery-store-api.glitch.me/products/4646
     * Status code should be 200
     * Header should be application/json; charset=utf-8
     * Body should contain id, category, name, manufacturer, price, current-stock,
     * inStock
     */

    @Test
    public void VerifyCodeHeaderStructure() {
        String url = "https://simple-grocery-store-api.glitch.me/products/";
        int ExpectedStatusCode = 200;
        int ExpectedProductId = 4646;
        String ExpectedHeader = "application/json; charset=utf-8";
        Response response = RestAssured.get(url + ExpectedProductId);
        int statusCode = response.getStatusCode();
        // verifying the status code - 200
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        String header = response.getContentType();
        // verifying the header - application/json; charset=utf-8
        Assert.assertEquals(header, ExpectedHeader);
        String body = response.getBody().asString();
        // verifying the structure of the response
        Assert.assertTrue(body.contains("id"));
        Assert.assertTrue(body.contains("category"));
        Assert.assertTrue(body.contains("name"));
        Assert.assertTrue(body.contains("manufacturer"));
        Assert.assertTrue(body.contains("price"));
        Assert.assertTrue(body.contains("current-stock"));
        Assert.assertTrue(body.contains("inStock"));
    }

    /**
     * Test5: Create a new cart, submit an empty POST request to the /carts endpoint
     * URL is https://simple-grocery-store-api.glitch.me/carts
     * Status code should be 201
     * Response should contain the cartId and created flag as true
     * if the cart is created successfully, verify the cartId response using the GET
     * URL is https://simple-grocery-store-api.glitch.me/carts/{cartId}
     * Status code should be 200
     * Response should contain the items and created date
     * Also, Verify the items for the cartId using the GET
     * URL is https://simple-grocery-store-api.glitch.me/carts/{cartId}/items
     * Status code should be 200
     * Response should contain the []
     */

    @Test
    public void VerifyCartsEndpoint() {
        String url = "https://simple-grocery-store-api.glitch.me/carts";
        int ExpectedStatusCode = 201;
        String ExpectedResponse = "true";
        Response response = RestAssured.post(url);
        int statusCode = response.getStatusCode();
        // verifying the status code - 201
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        // verifying the response - should contain the cartId and created flag as true
        String responseString = response.jsonPath().get("created").toString();
        Assert.assertEquals(responseString, ExpectedResponse);
        // store the cartId in a variable
        String cartId = response.jsonPath().get("cartId").toString();
        // verify the cartId response using the GET
        response = RestAssured.get(url + "/" + cartId);
        statusCode = response.getStatusCode();
        // verifying the status code - 200
        int ExpectedStatusCodeforGet = 200;
        Assert.assertEquals(statusCode, ExpectedStatusCodeforGet);
        // verifying the response - should contain the items and created date
        String responseStringforGet = response.getBody().asString();
        Assert.assertTrue(responseStringforGet.contains("items"));
        Assert.assertTrue(responseStringforGet.contains("created"));
        // verify the items for the cartId using the GET
        response = RestAssured.get(url + "/" + cartId + "/items");
        statusCode = response.getStatusCode();
        // verifying the status code - 200
        Assert.assertEquals(statusCode, ExpectedStatusCodeforGet);
        // verifying the response - should contain the []
        String responseStringforGetItems = response.getBody().asString();
        Assert.assertTrue(responseStringforGetItems.contains("[]"));
    }

    /**
     * Test6: Create a new cart, add the product with id 4646 item to the cart
     * Create new cart using the POST, URL is
     * https://simple-grocery-store-api.glitch.me/carts, store the cartId in a
     * variable cartId and status code should be 201
     * Add the product with id 4646 to the cart using the POST, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items, status code
     * should be 201
     * Verify the items for the cartId using the GET, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items, status code
     * should be 200
     */

    @Test
    public void VerifyCartItemsEndpoint() {
        String url = "https://simple-grocery-store-api.glitch.me/carts";
        int ExpectedStatusCode = 201;
        String ExpectedResponse = "true";
        // create new cart using the POST
        Response response = RestAssured.post(url);
        int statusCode = response.getStatusCode();
        // verifying the status code - 201
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        // verifying the response - should contain the cartId and created flag as true
        String responseString = response.jsonPath().get("created").toString();
        Assert.assertEquals(responseString, ExpectedResponse);
        // store the cartId in a variable
        String cartId = response.jsonPath().get("cartId").toString();
        // add the product with id 4646 to the cart using the POST
        String urlforPost = url + "/" + cartId + "/items";
        // request body: {"productId": 4646, "quantity": 1}
        String requestbody = "{\"productId\": 4646, \"quantity\": 1}";
        response = RestAssured.given().contentType("application/json").body(requestbody).post(urlforPost);
        statusCode = response.getStatusCode();
        // verifying the status code - 201
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        // verify the items for the cartId using the GET
        response = RestAssured.get(url + "/" + cartId + "/items");
        statusCode = response.getStatusCode();
        // verifying the status code - 200
        int ExpectedStatusCodeforGet = 200;
        Assert.assertEquals(statusCode, ExpectedStatusCodeforGet);
        // verifying the response and check for the product id 4646
        String responseStringforGetItems = response.getBody().asString();
        Assert.assertTrue(responseStringforGetItems.contains("4646"));
        System.out.println(responseStringforGetItems);
    }

    /**
     * Test7: Create a new cart, add the product with id 4646 item to the cart with
     * quantity 1,
     * replace the quantity of the product with id 4646 to 2 and verify the quantity
     * is updated,
     * Create new cart using the POST, URL is
     * https://simple-grocery-store-api.glitch.me/carts,
     * store the cartId in a variable cartId and status code should be 201
     * Add the product with id 4646 to the cart using the POST, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items,
     * request body: {"productId": 4646, "quantity": 1}, status code should be 201
     * Replace the quantity of the product with id 4646 to 2 using the PUT, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items/{productId},
     * request body: {"quantity": 2}, status code should be 200
     * Verify the items for the cartId using the GET, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items,
     * status code should be 200 and quantity should be 2
     */

    @Test
    public void VerifyCartItemsEndpointWithQuantity() {
        String url = "https://simple-grocery-store-api.glitch.me/carts";
        int ExpectedStatusCode = 201;
        int ProductId = 4646;
        String ExpectedResponse = "true";
        // create new cart using the POST and store the cartId in a variable cartId and
        // status code should be 201
        Response response = RestAssured.post(url);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        String responseString = response.jsonPath().get("created").toString();
        Assert.assertEquals(responseString, ExpectedResponse);
        String cartId = response.jsonPath().get("cartId").toString();
        // add the product with id 4646 to the cart using the POST and request body:
        // {"productId": 4646, "quantity": 1}
        String urlforPost = url + "/" + cartId + "/items";
        String requestbody = "{\"productId\": 4646, \"quantity\": 1}";
        response = RestAssured.given().contentType("application/json").body(requestbody).post(urlforPost);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        // verify if items are added to the cart using the GET
        response = RestAssured.get(url + "/" + cartId + "/items");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String responseStringforGetItems = response.jsonPath().get("productId").toString();
        Assert.assertEquals(responseStringforGetItems, "[" + ProductId + "]");
        String responseStringforGetItemsQuantity = response.jsonPath().get("quantity").toString();
        Assert.assertEquals(responseStringforGetItemsQuantity, "[" + 1 + "]");
        String responseStringforGetItemsId = response.jsonPath().get("[0].id").toString();
        System.out.println(responseStringforGetItemsId);
        // replace the quantity of the product with id 4646 to 2 using the PUT and
        // request body: {"quantity": 2}
        String urlforPut = url + "/" + cartId + "/items" + "/" + responseStringforGetItemsId;
        String requestbodyforPut = "{\"productId\": 4646, \"quantity\": 2}";
        response = RestAssured.given().contentType("application/json").body(requestbodyforPut).put(urlforPut);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 204);
        // verify the items has been updated using the GET and status code should be 200
        // and quantity should be 2
        response = RestAssured.get(url + "/" + cartId + "/items");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String responseStringforGetItemsQuantityAfterPut = response.jsonPath().get("productId").toString();
        Assert.assertEquals(responseStringforGetItemsQuantityAfterPut, "[" + ProductId + "]");
        String responseStringforGetItemsQuantityAfterPutQuantity = response.jsonPath().get("quantity").toString();
        Assert.assertEquals(responseStringforGetItemsQuantityAfterPutQuantity, "[" + 2 + "]");
    }

    /**
     * Test8: Create a new cart, add the product with id 4646 item to the cart with
     * quantity 1,
     * modify the quantity of the product with id 4646 to 2 and verify the quantity
     * is updated,
     * Create new cart using the POST, URL is
     * https://simple-grocery-store-api.glitch.me/carts,
     * store the cartId in a variable cartId and status code should be 201
     * Add the product with id 4646 to the cart using the POST, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items,
     * request body: {"productId": 4646, "quantity": 1}, status code should be 201
     * Modify the quantity of the product with id 4646 to 2 using the PATCH, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items/{productId},
     * request body: {"quantity": 2}, status code should be 200
     * Verify the items for the cartId using the GET, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items,
     * status code should be 200 and quantity should be 2
     */

    @Test
    public void VerifyModifyCartItems() {
        String url = "https://simple-grocery-store-api.glitch.me/carts";
        int ExpectedStatusCode = 201;
        int ProductId = 4646;
        String ExpectedResponse = "true";
        // create new cart using the POST and store the cartId in a variable cartId and
        // status code should be 201
        Response response = RestAssured.post(url);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        String responseString = response.jsonPath().get("created").toString();
        Assert.assertEquals(responseString, ExpectedResponse);
        String cartId = response.jsonPath().get("cartId").toString();
        // add the product with id 4646 to the cart using the POST and request body:
        // {"productId": 4646, "quantity": 1}
        String urlforPost = url + "/" + cartId + "/items";
        String requestbody = "{\"productId\": 4646, \"quantity\": 1}";
        response = RestAssured.given().contentType("application/json").body(requestbody).post(urlforPost);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        // verify if items are added to the cart using the GET
        response = RestAssured.get(url + "/" + cartId + "/items");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String responseStringforGetItems = response.jsonPath().get("productId").toString();
        Assert.assertEquals(responseStringforGetItems, "[" + ProductId + "]");
        String responseStringforGetItemsQuantity = response.jsonPath().get("quantity").toString();
        Assert.assertEquals(responseStringforGetItemsQuantity, "[" + 1 + "]");
        String responseStringforGetItemsId = response.jsonPath().get("[0].id").toString();
        System.out.println(responseStringforGetItemsId);
        // modify the quantity of the product with id 4646 to 2 using the PATCH and
        // request body: {"quantity": 2}
        String urlforPatch = url + "/" + cartId + "/items" + "/" + responseStringforGetItemsId;
        String requestbodyforPatch = "{\"productId\": 4646, \"quantity\": 2}";
        response = RestAssured.given().contentType("application/json").body(requestbodyforPatch).patch(urlforPatch);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 204);
        // verify the items has been updated using the GET and status code should be 200
        // and quantity should be 2
        response = RestAssured.get(url + "/" + cartId + "/items");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String responseStringforGetItemsQuantityAfterPatch = response.jsonPath().get("productId").toString();
        Assert.assertEquals(responseStringforGetItemsQuantityAfterPatch, "[" + ProductId + "]");
        String responseStringforGetItemsQuantityAfterPatchQuantity = response.jsonPath().get("quantity").toString();
        Assert.assertEquals(responseStringforGetItemsQuantityAfterPatchQuantity, "[" + 2 + "]");
    }

    /**
     * Test9: Create a new cart, add the product with id 4646 item to the cart with
     * quantity 1,
     * delete the product with id 4646 from the cart and verify the product is
     * deleted,
     * Create new cart using the POST, URL is
     * https://simple-grocery-store-api.glitch.me/carts,
     * store the cartId in a variable cartId and status code should be 201
     * Add the product with id 4646 to the cart using the POST, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items,
     * request body: {"productId": 4646, "quantity": 1}, status code should be 201
     * Delete the product with id 4646 from the cart using the DELETE, URL is
     * https://simple-grocery-store-api.glitch.me/carts/{cartId}/items/{productId},
     * status code should be 204
     */

    @Test
    public void VerifyDeleteCartItems() {
        String url = "https://simple-grocery-store-api.glitch.me/carts";
        int ExpectedStatusCode = 201;
        int ProductId = 4646;
        String ExpectedResponse = "true";
        // create new cart using the POST and store the cartId in a variable cartId and
        // status code should be 201
        Response response = RestAssured.post(url);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        String responseString = response.jsonPath().get("created").toString();
        Assert.assertEquals(responseString, ExpectedResponse);
        String cartId = response.jsonPath().get("cartId").toString();
        // add the product with id 4646 to the cart using the POST and request body:
        // {"productId": 4646, "quantity": 1}
        String urlforPost = url + "/" + cartId + "/items";
        String requestbody = "{\"productId\": 4646, \"quantity\": 1}";
        response = RestAssured.given().contentType("application/json").body(requestbody).post(urlforPost);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, ExpectedStatusCode);
        // verify if items are added to the cart using the GET
        response = RestAssured.get(url + "/" + cartId + "/items");
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        String responseStringforGetItems = response.jsonPath().get("productId").toString();
        Assert.assertEquals(responseStringforGetItems, "[" + ProductId + "]");
        String responseStringforGetItemsQuantity = response.jsonPath().get("quantity").toString();
        Assert.assertEquals(responseStringforGetItemsQuantity, "[" + 1 + "]");
        String responseStringforGetItemsId = response.jsonPath().get("[0].id").toString();
        System.out.println(responseStringforGetItemsId);
        // delete the product with id 4646 from the cart using the DELETE
        String urlforDelete = url + "/" + cartId + "/items" + "/" + responseStringforGetItemsId;
        response = RestAssured.delete(urlforDelete);
        statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 204);
        // verify the items has been deleted using the GET and status code should be 200
        response = RestAssured.get(url + "/" + cartId + "/items");
        statusCode = response.getStatusCode();
    }
}
