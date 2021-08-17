package com.example.app;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Entry {
    public static void main(String[] args){
        //asyncBuild();
        syncBuild();
    }

    private static void asyncBuild(){
        final int workerCount = 4; //1

        ExecutorService service = Executors.newFixedThreadPool(workerCount); //2

        try{
            var builder = new ApartmentBuilder(); //3

            for (int i = 0; i < workerCount; i++){ //4
                service.submit(builder::asyncBuildApartments); //5
            }
        } finally {
            service.shutdown(); //6
        }
    }

    private static void syncBuild(){ //7
        final int workerCount = 4;   //8

        ExecutorService service = Executors.newFixedThreadPool(workerCount); //9

        try {
            var builder = new ApartmentBuilder(); //10
            var barrier1 = new CyclicBarrier(workerCount,
                    () -> System.out.println("Done building 1st floor apartments!")); //11
            var barrier2 = new CyclicBarrier(workerCount,
                    () -> System.out.println("Done building 2nd floor apartments!")); //12

            for (int i = 0; i < workerCount; i++){ //13
                service.submit(() -> builder.syncBuildApartments(barrier1, barrier2)); //14
            }
        } finally {
            service.shutdown(); //15
        }
    }
}

class ApartmentBuilder {
    private void buildFirstFloorApt(){ //16
        System.out.println("Built an Apartment on 1st Floor.");
    }

    private void buildSecondFloorApt(){ //17
        System.out.println("Built an Apartment on 2nd Floor.");
    }

    private void buildThirdFloorApt(){ //18
        System.out.println("Built an Apartment on 3rd Floor.");
    }

    public void syncBuildApartments(CyclicBarrier barrier1, CyclicBarrier barrier2){ //19
        try {
            buildFirstFloorApt(); //20
            barrier1.await();     //21
            buildSecondFloorApt();//22
            barrier2.await();     //23
            buildThirdFloorApt(); //24
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void asyncBuildApartments(){ //25
            buildFirstFloorApt(); //26
            buildSecondFloorApt();//27
            buildThirdFloorApt(); //28
    }

}