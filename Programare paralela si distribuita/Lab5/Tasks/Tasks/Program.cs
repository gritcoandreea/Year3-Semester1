using System;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Tasks
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
                                          + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv: 56.0) Gecko/20100101 Firefox/56.0" +
                                          "\r\n"
                                          + "Accept: text/html,application/xhtml + xml,application/xml;q=0.9,*/*;q=0.8" +
                                          "\r\n"
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
            
            Task.Factory.StartNew(() => Connect(client, remoteEp))
                .ContinueWith(t => Send(t.Result.Result, HttpHeader))
                .ContinueWith(t => Receive(t.Result.Result))
                .ContinueWith(t => {Console.WriteLine(t.Result.Result);});
        }

        private Task<Socket> Connect(Socket client, EndPoint remoteEp)
        {
            var tcs = new TaskCompletionSource<Socket>();
            // Connect to the remote endpoint.  
            Console.WriteLine("CONNECT");
            client.BeginConnect(remoteEp,
                iar =>
                {
                    var s = (Socket) iar.AsyncState;

                    // Complete the connection.  
                    s.EndConnect(iar);

                    Console.WriteLine("Socket connected to {0}",
                        client.RemoteEndPoint);
                    try
                    {
                        tcs.TrySetResult(s);
                    }
                    catch (Exception ex)
                    {
                        tcs.TrySetException(ex);
                    }
                }, client);
            return tcs.Task;
        }

        private Task<string> Receive(Socket client)
        {
            Console.WriteLine("RECEIVE");
            var tcs = new TaskCompletionSource<string>();    
            try
            {
                // Create the state object.  
                var state = new Program {WorkSocket = client};
                // Begi receiving the data from the remote device.  
                client.BeginReceive(state.Buffer, 0, Program.BufferSize, 0,
                    iar =>
                    {
                        var st = (Program) iar.AsyncState;
                        var cl = state.WorkSocket;

                        // Read data from the remote device.  
                        var bytesRead = client.EndReceive(iar);
                        // There might be more data, so store the data received so far.  
                        st.Sb.Append(Encoding.ASCII.GetString(state.Buffer, 0, bytesRead));
                        // Signal that all bytes have been received.  
                        client.Shutdown(SocketShutdown.Both);
                        client.Close();
                        
                        tcs.SetResult(st.Sb.ToString());
                    }, state);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

            return tcs.Task;
        }

        private Task<Socket> Send(Socket client, string data)
        {
            var tcs = new TaskCompletionSource<Socket>();
            // Convert the string data to byte data using ASCII encoding.  
            var byteData = Encoding.ASCII.GetBytes(data);
            Console.WriteLine("SEND");
            // Begin sending the data to the remote device.  
            client.BeginSend(byteData, 0, byteData.Length, 0,
                iar =>
                {
                    Console.Write("AM INRAT IN CALBACK");
                    // Retrieve the socket from the state object.  
                    var cl= (Socket) iar.AsyncState;

                    // Complete sending the data to the remote device.  
                    var bytesSent = cl.EndSend(iar);
                    Console.WriteLine("Sent {0} bytes to server.", bytesSent);
                     
                    tcs.SetResult(cl);
                }, client);

            return tcs.Task;
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