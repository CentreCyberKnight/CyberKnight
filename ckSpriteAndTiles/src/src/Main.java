package ckSpriteAndTiles.src.src;
public class Main {

        /**
         * @param args
         */

        private static void unitMoveTest1()
        {
                boolean test;
                Build board;
                board = new Build();
                if (board.getHeight()!=4)
                {
                        System.out.print("heightTest ");
                }
                if (board.getWidth()!=4)
                {
                        System.out.print("widthTest ");
                }
                for(int i=0; i<4; i++)
                {
                        test =board.isLeft(i);
                        if (test==true)
                        {System.out.print("isLeftTest ");}
                }
                for(int i=4; i<16; i++)
                {
                        test =board.isLeft(i);
                        if (test==false)
                        {System.out.print("isLeftTest#2 ");}
                }
                for(int i=12; i<16; i++)
                {
                        test =board.isRight(i);
                        if (test==true)
                        {System.out.print("isRightTest ");}
                }
                for(int i=0; i<12; i++)
                {
                        test =board.isRight(i);
                        if (test==false)
                        {System.out.print("isRightTest#2 ");}
                }
                for(int i=0; i<3; i++)
                {
                        test =board.isUp(i);
                        if (test==false)
                        {System.out.print("isUpTest ");}
                }
                for(int i=4; i<7; i++)
                {
                        test =board.isUp(i);
                        if (test==false)
                        {System.out.print("isUpTest#1 ");}
                }
                for(int i=8; i<11; i++)
                {
                        test =board.isUp(i);
                        if (test==false)
                        {System.out.print("isUpTest#2 ");}
                }
                for(int i=12; i<15; i++)
                {
                        test =board.isUp(i);
                        if (test==false)
                        {System.out.print("isUpTest#3 ");}
                }
                test =board.isUp(3);
                if (test==true)
                {System.out.print("isUpTest#4 ");}
                test =board.isUp(7);
                if (test==true)
                {System.out.print("isUpTest#5 ");}
                test =board.isUp(11);
                if (test==true)
                {System.out.print("isUpTest#6 ");}
                test =board.isUp(15);
                if (test==true)
                {System.out.print("isUpTest#7 ");}
        }

        private static void unitMoveTest2()
        {
                boolean test;
                Build board;
                board = new Build();
                for(int i=1; i<4; i++)
                {
                        test =board.isDown(i);
                        if (test==false)
                        {System.out.print("isDownTest ");}
                }
                for(int i=5; i<8; i++)
                {
                        test =board.isDown(i);
                        if (test==false)
                        {System.out.print("isDownTest#1 ");}
                }
                for(int i=9; i<12; i++)
                {
                        test =board.isDown(i);
                        if (test==false)
                        {System.out.print("isDownTest#2 ");}
                }
                for(int i=13; i<16; i++)
                {
                        test =board.isDown(i);
                        if (test==false)
                        {System.out.print("isDownTest#3 ");}
                }
                test =board.isDown(0);
                if (test==true)
                {System.out.print("isDownTest#4 ");}
                test =board.isDown(4);
                if (test==true)
                {System.out.print("isDownTest#5 ");}
                test =board.isDown(8);
                if (test==true)
                {System.out.print("isDownTest#6 ");}
                test =board.isDown(12);
                if (test==true)
                {System.out.print("isDownTest#7 ");}
        }
        
        
        public static void main(String[] args)
        {
                unitMoveTest1();
                unitMoveTest2();
                System.out.print("yayayaya");
        }
}