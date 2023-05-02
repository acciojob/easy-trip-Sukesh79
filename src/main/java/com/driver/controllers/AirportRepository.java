package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {

    private HashMap<String, Airport> airportHashMap = new HashMap<>();
    private Set<City> citySet = new HashSet<>();
    private HashMap<Integer, Flight> flightHashMap = new HashMap<>();
    private HashMap<Integer, Passenger> passengerHashMap = new HashMap<>();
    private HashMap<Integer, List<Integer>> flightToPassenger = new HashMap<>();
    private HashMap<Integer, List<Integer>> passengerToFlight = new HashMap<>();
    private HashMap<Integer, Integer> flightRevenue = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, Integer>> passengerExpense = new HashMap<>();
    public void addAirport(Airport airport) throws Exception{
        if(airportHashMap.containsKey(airport.getAirportName())){
            throw new Exception("Airport already existed");
        }
        if(citySet.contains(airport.getCity())){
            throw new Exception("There is already a City Airport");
        }
        airportHashMap.put(airport.getAirportName(), airport);
        citySet.add(airport.getCity());
    }

    public String getLargestAirportName() {
        String maxName = "";
        int maxterminals = Integer.MIN_VALUE;

        for(Airport airport : airportHashMap.values()){
            if(airport.getNoOfTerminals() > maxterminals){
                maxterminals = airport.getNoOfTerminals();
                maxName = airport.getAirportName();
            }
            else if(airport.getNoOfTerminals() == maxterminals){
                if(airport.getAirportName().compareTo(maxName) < 0){
                    maxName = airport.getAirportName();
                }
            }
        }
        return maxName;

    }

    public void addFlight(Flight flight) throws Exception {
        if(flightHashMap.containsKey(flight.getFlightId())){
            throw new Exception("Flight Id already exists");
        }
        flightHashMap.put(flight.getFlightId(), flight);
    }

    public double getShortestDuration(City fromCity, City toCity) {

        double min = Double.MAX_VALUE;
        for(Flight flight : flightHashMap.values()){
            if(flight.getFromCity() == fromCity && flight.getToCity() == toCity){
                min = Math.min(min, flight.getDuration());
            }
        }

        return min;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
       int people = 0;
       if(!airportHashMap.containsKey(airportName)) return 0;

       City city = airportHashMap.get(airportName).getCity();

       for(Flight flight : flightHashMap.values()){
           if(flight.getFlightDate() == date && (flight.getFromCity() == city || flight.getToCity() == city)){
               people += flightToPassenger.get(flight.getFlightId()).size();
           }
       }

       return people;
    }

    public void addPassenger(Passenger passenger) throws Exception{
        if(passengerHashMap.containsKey(passenger.getPassengerId())){
            throw new Exception("Passenger already exists");
        }
        passengerHashMap.put(passenger.getPassengerId(), passenger);
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        if (flightHashMap.containsKey(flightId)) {
            return flightHashMap.get(flightId).getFromCity().name();
        }
        return null;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        if(flightToPassenger.containsKey(flightId) && flightHashMap.containsKey(flightId)
                && flightToPassenger.get(flightId).size() >= flightHashMap.get(flightId).getMaxCapacity()){
            return "FAILURE";
        }
        if(!flightHashMap.containsKey(flightId)) return "FAILURE";

        List<Integer> list = flightToPassenger.getOrDefault(flightId, new ArrayList<>());
        if(list.contains(passengerId)) return "FAILURE";

        list.add(passengerId);
        flightToPassenger.put(flightId, list);

        passengerToFlight.get(passengerId).add(flightId);

        int fare = calculateFlightFare(flightId);
        flightRevenue.put(flightId, flightRevenue.getOrDefault(flightId, 0) + fare);

        HashMap<Integer, Integer> passengerBooking = passengerExpense.getOrDefault(passengerId, new HashMap<>());
        passengerBooking.put(flightId, fare);
        passengerExpense.put(passengerId, passengerBooking);

        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        if(!flightToPassenger.containsKey(flightId) || !passengerToFlight.containsKey(passengerId)) return "FAILURE";

        if(!flightToPassenger.get(flightId).contains(passengerId) || !passengerToFlight.get(passengerId).contains(flightId))
            return "FAILURE";

        flightToPassenger.get(flightId).remove(passengerId);
        passengerToFlight.get(passengerId).remove(flightId);

        flightRevenue.put(flightId, flightRevenue.get(flightId) - passengerExpense.get(passengerId).get(flightId));
        return "SUCCESS";
    }

    public int calculateFlightFare(Integer flightId) {
        int fare = 3000;
        if(flightToPassenger.containsKey(flightId)){
            fare = fare + (flightToPassenger.get(flightId).size()) * 50;
        }

        return fare;
    }

    public int countPassengerBookings(Integer passengerId) {
        return passengerToFlight.getOrDefault(passengerId, new ArrayList<>()).size();
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return flightRevenue.get(flightId);
    }
}
