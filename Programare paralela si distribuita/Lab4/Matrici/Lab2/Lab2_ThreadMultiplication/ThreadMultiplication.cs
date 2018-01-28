using System;
using System.Diagnostics;
using System.Threading;
using Matrix = UserDefinedMatrix.Matrix;

namespace Lab2_ThreadMultiplication
{
    public struct RowCol
    {
        public int row;
        public int col;
    }
    class Program
    {
        public const int M = 2000; // Number of lines of A
        public const int K = 250; // Number of cols of A and lines of B
        public const int N = 2000; // Number of cols of B
        public const int ThreadNumber = 8; // the number of threads (1 <= ThreadNumber <= M*N)
        public static int NumberOfOperations;
        public static ManualResetEvent resetEvent = new ManualResetEvent(false);
        public static Matrix A = new Matrix(M, K);
        public static Matrix B = new Matrix(K, N);
        public static Matrix C = new Matrix(M, N);
        //public static Matrix A = new Matrix();
        //public static Matrix B = new Matrix();
        //public static Matrix C = new Matrix(4, 2);
        private static readonly Mutex m = new Mutex();

        static void Main(string[] args)
        {
            ThreadPool.SetMinThreads(ThreadNumber, 2);
            ThreadPool.SetMaxThreads(ThreadNumber, 2);

            //A.GetFixedValues1();
            //B.GetFixedValues2();
            A.RandomValues();
            B.RandomValues();

            // Check if multiplication is possible
            if (A.Columns != B.Rows)
            {
                Console.WriteLine("Matrix multiplication is not possible.");
                Console.ReadLine();
                Environment.Exit(0);
            }
            NumberOfOperations = M * N;
            //NumberOfOperations = 8;
            Console.WriteLine("Stopwatch started");
            var watch = Stopwatch.StartNew();
            for (var i = 0; i < C.Rows; i++)
            {
                for (var j = 0; j < C.Columns; j++)
                {
                    RowCol rowcol;

                    rowcol.row = i;
                    rowcol.col = j;
                    
                    ThreadPool.QueueUserWorkItem(DoMultiplication, rowcol);
                }
            }

            resetEvent.WaitOne();
            watch.Stop();
            //C.Print();
            Console.WriteLine("Computation finished in: {0} milliseconds", watch.ElapsedMilliseconds);
            Console.ReadLine();

        }

        public static void DoMultiplication(object param)
        {
            RowCol data = (RowCol) param;
            var row = A.GetRow(data.row);
            var col = B.GetColumn(data.col);
            var sum = 0;
            //Console.WriteLine("Processing row {0}, col {1}", data.row, data.col);
            for(var i = 0; i < row.Length; i++)
            {
                sum += row[i] * col[i];
            }
            C[data.row, data.col] = sum;

            // Check for remaining computations
            m.WaitOne();
            NumberOfOperations--;
            if (NumberOfOperations == 0)
            {
                resetEvent.Set();
            }
            m.ReleaseMutex();
        }
    }
}
