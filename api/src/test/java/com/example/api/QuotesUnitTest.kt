package com.example.api

import com.example.api.network.ApiClient
import com.example.api.network.ApiService
import com.example.api.network.responses.SignInResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class QuotesUnitTest {

    @Test
    fun `POST Login User`() = runBlocking {
        val api = ApiClient.getApiService(ApiService::class.java)
        val response: SignInResponse = api.signIn("probelalkhan1@gmail.com", "12345678")
        Assert.assertNotNull(response.user)
    }

}

//@Test
//fun `GET Articles`() {
//    runBlocking {
//        val apiArticlesResponse = ConduitClient.getApiService()!!.getArticles()
//        Assert.assertNotNull(apiArticlesResponse.body()?.articles)
//    }
//}
//
//@Test
//fun `GET Articles by Author`() {
//    runBlocking {
//        val apiArticlesResponse = ConduitClient.getApiService()!!.getArticles(author = "444")
//        Assert.assertNotNull(apiArticlesResponse.body()?.articles)
//    }
//}
//
//@Test
//fun `GET Articles by Tag`() {
//    runBlocking {
//        val apiArticlesResponse: Response<ArticlesResponse> = ConduitClient.getApiService()!!.getArticles(tag = "dragons")
//        Assert.assertNotNull(apiArticlesResponse.body()?.articles)
//    }
//}
//
//@Test
//fun `GET my articles`() {
//    runBlocking {
//        val resp: Response<ArticlesResponse>? = ConduitClient.getAuthApiService()?.getFeedArticles()
//        Assert.assertNotNull(resp?.body()?.articles)
//    }
//}
//
//@Test
//fun `POST users - create user`() {
//
//    val userCred = UserSignupCred(
//        username = "testUserName${Random.nextInt(0, 1000)}",
//        email = "testemail${Random.nextInt(0, 1000)}@test.com",
//        password = "testPass${Random.nextInt(0, 1000)}"
//    )
//
//    runBlocking {
//        val resp: Response<UserResponse>? = ConduitClient.getApiService()?.signUpUser(SignUpRequest(userCred))
//        assertEquals(userCred.username, resp?.body()?.user?.username)
//    }
//}