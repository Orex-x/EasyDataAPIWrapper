using EasyDataApiWrapper.models;
using Newtonsoft.Json;
using System.Net;

namespace EasyDataApiWrapper.services
{
    public class ApiClient
    {

        private string _port = "5000";
        private string _host = "localhost";
        private string _protocol = "http";
        private string _baseUrl = "api/easydata/models/__default/sources";
        private DateTimeZoneHandling _dateTimeZoneHandling = DateTimeZoneHandling.Utc;
        private string _jsonCharset = "utf-8";

        public string Port
        {
            set { _port = value; }
        }

        public string Host
        {
            set { _host = value; }
        }

        public string Protocol
        {
            set { _protocol = value; }
        }

        public string BaseUrl
        {
            set { _baseUrl = value; }
        } 
        
        public string JsonCharset
        {
            set { _jsonCharset = value; }
        }

        public DateTimeZoneHandling DateTimeZoneHandling 
        {
            set { _dateTimeZoneHandling = value; }
        }

        private string GenerateURI<T>(Crud crud)
        {
            return $"{_protocol}://{_host}:{_port}/{_baseUrl}/{typeof(T).Name}/{crud}";
        }


        private string SendRequest(string? body, string uri, Method method)
        {
            try
            {
                var httpWebRequest = (HttpWebRequest) WebRequest.Create(uri);
                httpWebRequest.ContentType = $"application/json; charset={_jsonCharset}";
                httpWebRequest.Method = method.ToString();

                if (body?.Length > 0)
                {
                    using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
                    {
                        streamWriter.Write(body);
                        streamWriter.Flush();
                    }
                }


                var httpResponse = (HttpWebResponse)httpWebRequest.GetResponse();
                using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
                {
                    var result = streamReader.ReadToEnd();
                    return result;
                }
            }
            catch (Exception ee)
            {
                return ee.ToString();
            }
        }


        public List<T>? Get<T>()
        {
            try
            {
                string uri = GenerateURI<T>(Crud.get);
                string json = SendRequest(null, uri, Method.POST);
                return JsonConvert.DeserializeObject<List<T>>(json);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }
            return null;
        }


        public int Create<T>(T obj)
        {
            try
            {
                string json = JsonConvert.SerializeObject(obj, new JsonSerializerSettings
                {
                    DateTimeZoneHandling = _dateTimeZoneHandling
                });

                string uri = GenerateURI<T>(Crud.create);
                string jsonRequest = SendRequest(json, uri, Method.POST);

                dynamic? jsonObj = JsonConvert.DeserializeObject(jsonRequest);
                return Convert.ToInt32(jsonObj?["record"]["Id"]);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }
            return 0;
        }

        public int Update<T>(T obj)
        {
            try
            {
                string json = JsonConvert.SerializeObject(obj, new JsonSerializerSettings
                {
                    DateTimeZoneHandling = DateTimeZoneHandling.Utc
                });

                string uri = GenerateURI<T>(Crud.update);
                string jsonRequest = SendRequest(json, uri, Method.POST);

                dynamic? jsonObj = JsonConvert.DeserializeObject(jsonRequest);
                return Convert.ToInt32(jsonObj?["record"]["Id"]);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }

            return 0;
        }


        public void Delete<T>(int id)
        {
            try
            {
                string json = "{ Id: " + id + " }";

                string uri = GenerateURI<T>(Crud.delete);
                string jsonRequest = SendRequest(json, uri, Method.DELETE);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }
        }
    }
}
