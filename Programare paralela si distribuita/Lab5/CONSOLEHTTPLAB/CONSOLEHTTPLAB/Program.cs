using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace CONSOLEHTTPLAB
{
// State object for receiving data from remote device.  
    public class Program
    {
        // Client socket.  
        public Socket WorkSocket;
        // Size of receive buffer.  
        public const int BufferSize = 256;
        // Receive buffer.  
        public readonly byte[] Buffer = new byte[BufferSize];
        // Received data string.  
        public readonly StringBuilder Sb = new StringBuilder();
    }

    public class AsynchronousClient
    {
        // The port number for the remote device.  
        private const int Port = 80;
        private const string HttpHeader = "HEAD / HTTP/1.1" + "\r\n"
                                          + "Host: www.theuselessweb.com" + "\r\n"
                                          + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv: 56.0) Gecko/20100101 Firefox/56.0" + "\r\n"
                                          + "Accept: text/html,application/xhtml + xml,application/xml;q=0.9,*/*;q=0.8" + "\r\n"
                                          + "Accept-Language: en-US,en;q=0.5" + "\r\n"
                                          + "Accept-Encoding: gzip, deflate, br" + "\r\n"
                                          + "Connection: keep-alive" + "\r\n"
                                          + "Upgrade-Insecure-Requests: 1" + "\r\n"
                                          + "Cache-Control: max-age=0" + "\r\n\r\n";

   
        // The response from the remote device.  
        private static string _response = string.Empty;

        private ManualResetEvent Event { get; }

        private AsynchronousClient(ManualResetEvent Event)
        {
            this.Event = Event;
        }

        private void StartClient()
        {
            var ipHostInfo = Dns.GetHostEntry("www.theuselessweb.com");
            var ipAddress = ipHostInfo.AddressList[0];
            var remoteEp = new IPEndPoint(ipAddress, Port);
            var client = new Socket(ipAddress.AddressFamily,
                SocketType.Stream, ProtocolType.Tcp);
            Connect(client, remoteEp);
        }

        private void Connect(Socket client, EndPoint remoteEp)
        {
                
            // Connect to the remote endpoint.  
        
            client.BeginConnect(remoteEp,
                ConnectCallback, client);

        }

        private void ConnectCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object.  
                var client = (Socket)ar.AsyncState;

                // Complete the connection.  
                client.EndConnect(ar);

                Console.WriteLine("Socket connected to {0}",
                    client.RemoteEndPoint);

                // Signal that the connection has been made.  
                Send(client, HttpHeader);

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private void Receive(Socket client)
        {
            try
            {
                // Create the state object.  
                var state = new Program {WorkSocket = client};
                // Begi receiving the data from the remote device.  
                client.BeginReceive(state.Buffer, 0, Program.BufferSize, 0,
                    ReceiveCallback, state);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private void ReceiveCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the state object and the client socket   
                // from the asynchronous state object.  
                var state = (Program)ar.AsyncState;
                var client = state.WorkSocket;

                // Read data from the remote device.  
                var bytesRead = client.EndReceive(ar);
                if (bytesRead > 0)
                {
                    // There might be more data, so store the data received so far.  
                    state.Sb.Append(Encoding.ASCII.GetString(state.Buffer, 0, bytesRead));

                    // Get the rest of the data.  
                    client.BeginReceive(state.Buffer, 0, Program.BufferSize, 0,
                        ReceiveCallback, state);
                }
                else
                {
                    // All the data has arrived; put it in response.  
                    if (state.Sb.Length > 1)
                    {
                        _response = state.Sb.ToString();
                    }
                    // Signal that all bytes have been received.  
                    client.Shutdown(SocketShutdown.Both);
                    client.Close();
                    Console.WriteLine(_response);
                    Event.Set();

                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        private void Send(Socket client, string data)
        {
            // Convert the string data to byte data using ASCII encoding.  
            var byteData = Encoding.ASCII.GetBytes(data);

            // Begin sending the data to the remote device.  
            client.BeginSend(byteData, 0, byteData.Length, 0,
                SendCallback, client);
        }

        private void SendCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object.  
                var client = (Socket)ar.AsyncState;

                // Complete sending the data to the remote device.  
                var bytesSent = client.EndSend(ar);
                Console.WriteLine("Sent {0} bytes to server.", bytesSent);

                // Signal that all bytes have been sent.  
                Receive(client);

            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        public static int Main()
        {
            var Event = new ManualResetEvent(false);
            var client = new AsynchronousClient(Event);
            client.StartClient();

            Event.WaitOne();
            return 0;
        }
    }
}