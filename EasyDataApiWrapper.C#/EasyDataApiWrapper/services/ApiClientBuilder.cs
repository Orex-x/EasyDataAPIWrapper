using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EasyDataApiWrapper.services
{
    public class ApiClientBuilder
    {
        private readonly ApiClient _apiClient = new ApiClient();

        public ApiClientBuilder Init()
        {
            return new ApiClientBuilder();
        }

        public ApiClient Build() => _apiClient;

        public ApiClientBuilder SetHost(string host)
        {
            _apiClient.Host = host;
            return this;
        }

        public ApiClientBuilder SetPort(int port)
        {
            _apiClient.Port = port.ToString();
            return this;
        }

        public ApiClientBuilder SetProtocol(string protocol)
        {
            _apiClient.Protocol = protocol;
            return this;
        }

        public ApiClientBuilder UseHttps()
        {
            _apiClient.Protocol = "https";
            return this;
        }

        public ApiClientBuilder SetBaseUrl(string url)
        {
            _apiClient.BaseUrl = url;
            return this;
        }

        public ApiClientBuilder SetDateFormat(DateTimeZoneHandling format)
        {
            _apiClient.DateTimeZoneHandling = format;
            return this;
        }

        public ApiClientBuilder SetJsonCharset(string charset)
        {
            _apiClient.JsonCharset = charset;
            return this;
        }
    }
}
