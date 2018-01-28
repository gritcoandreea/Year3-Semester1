using System;
using System.Diagnostics;
using System.Threading;
using Matrix = UserDefinedMatrix.Matrix;
namespace Lab2_ThreadAddition
{
    public struct RowCol
    {
        public int row;
        public int col;
    }
    class ThreadAddition
    {
        public const int M = 20; // Number of lines of A
        public const int N = 30; // Number of cols of B
        public const int ThreadNumber = 5; // the number of threads (1 <= ThreadNumber <= M*N)
        public static int NumberOfOperations;
        public static ManualResetEvent resetEvent = new ManualResetEvent(false);
        //public static Matrix A = new Matrix(M, N);
        //public static Matrix B = new Matrix(M, N);
        //public static Matrix C = new Matrix(M, N);
        public static Matrix A = new Matrix();
        public static Matrix B = new Matrix();
        public static Matrix C = new Matrix(4, 4);
        private static readonly Mutex m = new Mutex();

        static void Main(string[] args)
        {
            ThreadPool.SetMinThreads(ThreadNumber, 2);
            ThreadPool.SetMaxThreads(ThreadNumber, 2);

            A.GetFixedValues1();
            B.GetFixedValues1();
            //A.RandomValues();
            //B.RandomValues();

            // Check if multiplication is possible
            if (A.Rows != B.Rows || A.Columns != B.Columns)
            {
                Console.WriteLine("Matrix addition is not possible.");
                Console.ReadLine();
                Environment.Exit(0);
            }
            //NumberOfOperations = M * N;
            NumberOfOperations = 8;
            Console.WriteLine("Stopwatch started");
            var watch = Stopwatch.StartNew();
            for (var i = 0; i < C.Rows; i++)
            {
                for (var j = 0; j < C.Columns; j++)
                {
                    RowCol rowcol;

                    rowcol.row = i;
                    rowcol.col = j;

                    ThreadPool.QueueUserWorkItem(DoAddition, rowcol);
                }
            }


            resetEvent.WaitOne();
            watch.Stop();
            C.Print();
            Console.WriteLine("Computation finished in: {0} milliseconds", watch.ElapsedMilliseconds);
            Console.ReadLine();

        }

        public static void DoAddition(object param)
        {
            RowCol data = (RowCol)param;
            var valueA = A[data.row, data.col];
            var valueB = B[data.row, data.col];

            C[data.row, data.col] = valueA + valueB;

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
