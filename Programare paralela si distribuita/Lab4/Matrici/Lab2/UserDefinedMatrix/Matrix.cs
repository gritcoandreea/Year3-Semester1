using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UserDefinedMatrix
{
    public class Matrix
    {
        public int Rows { get; set; }
        public int Columns { get; set; }
        private int[,] arr;
        public Matrix(int rows, int cols)
        {
            Rows = rows;
            Columns = cols;
            arr = new int[Rows, Columns];
        }
        public Matrix() { }
        public int[] GetColumn(int i)
        {
            int[] res = new int[Rows];
            for (int j = 0; j < Rows; j++)
                res[j] = arr[j, i];
            return res;
        }
        public int[] GetRow(int i)
        {
            int[] res = new int[Columns];
            for (int j = 0; j < Columns; j++)
                res[j] = arr[i, j];
            return res;
        }
        public int this[int i, int j]
        {
            get => arr[i, j];
            set => arr[i, j] = value;
        }
        public Matrix RandomValues()
        {
            Random rnd = new Random();
            for (int i = 0; i < Rows; i++)
            for (int j = 0; j < Columns; j++)
                arr[i, j] = rnd.Next(100);
            return this;
        }

        public Matrix GetFixedValues1()
        {
            Rows = 4;
            Columns = 4;
            arr = new int[4,4] { { 2, 3, 4, 5 }, { 1, 2, 3, 4}, { 3, 4, 2, 1 }, {4, 1, 2,1 } };
            return this;
        }

        public Matrix GetFixedValues2()
        {
            Rows = 4;
            Columns = 2;
            arr = new int[4, 2] { { 2, 3 }, { 1, 2 }, { 3, 4 }, { 4, 1 } };
            return this;
        }

        public void Print()
        {
            for (int i = 0; i < Rows; i++)
            {
                for (int j = 0; j < Columns; j++)
                    Console.Write(arr[i, j] + " ");
                Console.WriteLine();
            }
        }
    }
}
