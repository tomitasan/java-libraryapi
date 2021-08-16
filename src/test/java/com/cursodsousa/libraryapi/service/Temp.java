package com.cursodsousa.libraryapi.service;
import java.io.IOException;
import java.util.Scanner;

public class Temp {

        public static void main(String[] args) throws IOException {
            Scanner key = new Scanner(System.in);
            int A, B, C, D;

            A = key.nextInt();
            B = key.nextInt();
            C = key.nextInt();
            D = key.nextInt();
            key.nextLine();
            key.close();

            if((B > C) && (D > A) && ((C + D) > (A +B)) && C > 0 && D > 0 && (A%2==0)){
                System.out.println("Valores aceitos");
            }
            else{
                System.out.println("Valores nao aceitos");
            }
        }

    }
