package UserDetailsZoomInfo;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import Files.payload;
public class Login {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//given,when,then
		
//login
RestAssured baseURI="https://insentstaging.api.insent.ai";
String loginResponse=given().log().all().header("Content-Type","application/json")
.body(payload.Login())
.when().post("/app/login")
.then().assertThat().statusCode(200).extract().response().asString();
System.out.println(loginResponse);
JsonPath js=new JsonPath(loginResponse);//parsing json
String tokenID=js.getString("token");
System.out.println("Token extracted"+tokenID);

//get details
String userResponse=given().log().all().header("Authorization","Bearer"+tokenID,"Content-Type",ContentType.JSON,"Accept",ContentType.JSON)
.when().get("/app/user/details")
.then().assertThat().statusCode(200).extract().response().asString();
JsonPath js1=ReUsableMethod.rawToJson(userResponse);//parsing json
String actualName=js1.getString("name");
String actualPhone=js1.getString("phone");
Assert.assertEquals(actualName,"nirdosh chauhan");
Assert.assertEquals(actualPhone,"9898163198");

//update user
String updateUserResponse= given().log().all().header("Authorization","Bearer"+tokenID,"Content-Type",ContentType.JSON,"Accept",ContentType.JSON)
.body(payload.UpdateUser())
.when().post("/app/user/details")
.then().log().all().assertThat().statusCode(200).extract().response().asString();
	
//validate updated user
String updatedUserResponse= given().log().all().header("Authorization","Bearer"+tokenID,"Content-Type",ContentType.JSON,"Accept",ContentType.JSON)
.when().get("/app/user/details")
.then().log().all().assertThat().statusCode(200).extract().response().asString();
JsonPath js2=ReUsableMethod.rawToJson(updatedUserResponse);//parsing json
String actualUpdatedPhone=js2.getString("phone");
Assert.assertEquals(actualUpdatedPhone,"9898163198");

//logout user
String logoutResponse=given().log().all().header("Authorization","Bearer"+tokenID,"Content-Type",ContentType.JSON,"Accept",ContentType.JSON)
.when().get("/app/user/details")
.then().assertThat().statusCode(200).extract().response().asString();
System.out.println(logoutResponse);
}
}
