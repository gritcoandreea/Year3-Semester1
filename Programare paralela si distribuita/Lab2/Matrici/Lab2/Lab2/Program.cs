using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab2
{
    class Program
    {
        private static void GenerateRandomMatrix(int m, int n, int[,] x)
        {
            Random r = new Random();
            for (int i = 0; i < m; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    x[i, j] = r.Next(1, 9);
                }
            }
        }

        private static void PrintMatrix(int m, int n, int[,] x)
        {
            for (int i = 0; i < m; i++)
            {
                Console.WriteLine();
                for (int j = 0; j < n; j++)
                {
                    Console.Write(x[i,j]);
                    Console.Write(' ');
                }
            }
            Console.WriteLine('\n');
        }
        
        static void Main(string[] args)
        {
            int l1 = 3, l2 = 2;
            int c1 = 2, c2 = 3;
            int[,] a = new int[l1,c1];
            int[,] b = new int[l2,c2];
            int[,] c = new int[l1,c2];
            
            GenerateRandomMatrix(l1,c1,a);
            GenerateRandomMatrix(l2,c2,b);


            PrintMatrix(l1,c1,a);
            PrintMatrix(l2,c2,b);
            Console.ReadLine();
        }
    }
}
