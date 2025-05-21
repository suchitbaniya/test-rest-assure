package Telecom;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Ncell {

    private String baseURI = "https://test0.moco.com.np/v400";
    private String deviceId = "ED0B83F1-0AED-45E4-B958-1AF26806053D";
    private String authToken;

    @BeforeClass
    public void loginAndGetAuthToken() {
        // Login and get X-MOCO-AUTH-TOKEN header
        Response response = given()
                .baseUri(baseURI)
                .header("X-MOCO-DEVICE", deviceId)
                .formParam("platform", "IOS")
                .formParam("id", "9860933917")
                .formParam("mpin", "123456")
                .formParam("model", "iphone10,5")
                .formParam("pushToken", "abcd")
                .when()
                .post("/user/authentication")
                .then()
                .statusCode(200)
                .extract()
                .response();

        authToken = response.header("X-MOCO-AUTH-TOKEN");
        System.out.println("Auth Token obtained: " + authToken);
    }

    
    @Test(priority = 1)
    public void getAvailableDataPacks_Success_1() {
        given()
            .baseUri(baseURI)
            .header("X-MOCO-DEVICE", deviceId)
            .header("X-MOCO-AUTH-TOKEN", authToken)
        .when()
            .get("/billpayment/telecom/ncell/pack")
        .then()
            .statusCode(200)
            .log().all();
    }

    
    @Test(priority = 2)
    public void getAvailableDataPacks_Unauthorized_2() {
        given()
            .baseUri(baseURI)
            .header("X-MOCO-DEVICE", deviceId)
            .header("X-MOCO-AUTH-TOKEN", "INVALID_TOKEN")
        .when()
            .get("/billpayment/telecom/ncell/pack")
        .then()
            .statusCode(403)
            .log().all();
    }

   
    @Test(priority = 3)
    public void getAvailableDataPacks_MissingHeaders_3() {
        given()
            .baseUri(baseURI)
        .when()
            .get("/billpayment/telecom/ncell/pack")
        .then()
            .statusCode(400)  
            .log().all();
    }

    @Test(priority = 4)
    public void getDataPackById_Valid_4() {
        String packId = "DP123"; 

        given()
            .baseUri(baseURI)
            .header("X-MOCO-DEVICE", deviceId)
            .header("X-MOCO-AUTH-TOKEN", authToken)
        .when()
            .get("/billpayment/telecom/ncell/pack/" + packId)
        .then()
            .statusCode(200)
            .log().all();
    }

 
    @Test(priority = 5)
    public void getDataPackById_Invalid_5() {
        String invalidPackId = "INVALID_ID";

        given()
            .baseUri(baseURI)
            .header("X-MOCO-DEVICE", deviceId)
            .header("X-MOCO-AUTH-TOKEN", authToken)
        .when()
            .get("/billpayment/telecom/ncell/pack/" + invalidPackId)
        .then()
            .statusCode(404)
            .log().all();
    }
}
