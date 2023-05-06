package services;

public class ApiClientBuilder {
    private ApiClient _apiClient = new ApiClient();

    public static ApiClientBuilder init()
    {
        return new ApiClientBuilder();
    }

    public ApiClient build() {
     return  _apiClient;
    }

    public ApiClientBuilder setHost(String host)
    {
        _apiClient.setHost(host);
        return this;
    }

    public ApiClientBuilder setPort(int port)
    {
        _apiClient.setPort(String.valueOf(port));
        return this;
    }

    public ApiClientBuilder setProtocol(String protocol)
    {
        _apiClient.setProtocol(protocol);
        return this;
    }

    public ApiClientBuilder useHttps()
    {
        _apiClient.setProtocol("https");
        return this;
    }

    public ApiClientBuilder setBaseUrl(String url)
    {
        _apiClient.setBaseUrl(url);
        return this;
    }

    public ApiClientBuilder setDateFormat(String format)
    {
        _apiClient.setDateFormat(format);
        return this;
    }

    public ApiClientBuilder setJsonCharset(String charset)
    {
        _apiClient.setJsonCharset(charset);
        return this;
    }
}
