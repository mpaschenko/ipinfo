package com.uc.ipinfo

import com.uc.ipinfo.data.datasource.DataSource
import com.uc.ipinfo.data.datasource.DataSourceImpl
import com.uc.ipinfo.data.remote.IpInfoApi
import com.uc.ipinfo.data.repository.Repository
import com.uc.ipinfo.data.repository.RepositoryImpl
import com.uc.ipinfo.domain.model.IpInfo
import com.uc.ipinfo.domain.model.Status
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Response

class RepositoryImplTest {

    @Mock
    private lateinit var mockDataSource: DataSource

    @Mock
    private lateinit var mockIpInfoApi: IpInfoApi

    private lateinit var repository: Repository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = RepositoryImpl(mockDataSource)
    }

    @Test
    fun `getIPInfo success`() = runBlocking {
        // Arrange
        val ipAddress = "8.8.8.8"
        val expectedResult = IpInfo(status = Status.SUCCESS, message = "Success")

        `when`(mockDataSource.getIpInfo(ipAddress)).thenReturn(expectedResult)

        // Act
        val result = repository.getIPInfo(ipAddress)

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getIPInfo using mock IpInfoApi success`() = runBlocking {
        // Arrange
        val ipAddress = "8.8.8.8"
        val expectedResult = IpInfo(status = Status.SUCCESS, message = "Success")

        `when`(mockIpInfoApi.getIpInfo(ipAddress)).thenReturn(
            CallMock(Response.success(expectedResult))
        )
        val dataSource = DataSourceImpl(mockIpInfoApi)

        // Act
        val result = dataSource.getIpInfo(ipAddress)

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getIPInfo using mock IpInfoApi failure`() = runBlocking {
        // Arrange
        val ipAddress = "invalid-ip"
        val errorResponse = Response.error<IpInfo>(400, "Bad Request".toResponseBody())

        `when`(mockIpInfoApi.getIpInfo(ipAddress)).thenReturn(
            CallMock(errorResponse)
        )
        val dataSource = DataSourceImpl(mockIpInfoApi)

        // Act
        val result = dataSource.getIpInfo(ipAddress)

        // Assert
        assertEquals(Status.FAILURE, result?.status)
        assertEquals("Bad Request", result?.message)
    }

    @Test
    fun `getIPInfo using mock IpInfoApi failure with exception`() = runBlocking {
        // Arrange
        val ipAddress = "8.8.8.8"
        val expectedResult = IpInfo(status = Status.FAILURE, message = "Network error")

        `when`(mockIpInfoApi.getIpInfo(ipAddress)).thenReturn(
            CallMock(Response.success(expectedResult), true)
        )
        val dataSource = DataSourceImpl(mockIpInfoApi)

        // Act
        val result = dataSource.getIpInfo(ipAddress)

        // Assert
        assertEquals(expectedResult, result)
    }

    class CallMock<T>(private val response: Response<T>, private val throwException: Boolean = false) : Call<T> {
        override fun enqueue(callback: retrofit2.Callback<T>) {
            callback.onResponse(this, response)
        }
        override fun clone(): Call<T> = this
        override fun execute(): Response<T> {
            return if (throwException) {
                throw Exception("Network error")
            } else {
                response
            }
        }
        override fun isExecuted() = false
        override fun cancel() { }
        override fun isCanceled() = false
        override fun request(): Request {
            TODO("Not yet implemented")
        }
        override fun timeout(): Timeout = Timeout.NONE
    }
}
