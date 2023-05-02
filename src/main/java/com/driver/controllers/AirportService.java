package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AirportService {

    @Autowired
    AirportRepository airportRepository;


    public void addAirport(Airport airport) throws Exception {
        airportRepository.addAirport(airport);
    }

    public String getLargestAirportName() {
        return airportRepository.getLargestAirportName();
    }

    public void addFlight(Flight flight) throws Exception {
            airportRepository.addFlight(flight);
    }

    public double getShortestDuration(City fromCity, City toCity) {
        return airportRepository.getShortestDuration(fromCity, toCity);

    }

    public int getNumberOfPeopleOn(Date date, String airportName) {

        return airportRepository.getNumberOfPeopleOn(date, airportName);
    }

    public void addPassenger(Passenger passenger) throws Exception {
            airportRepository.addPassenger(passenger);
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        return airportRepository.getAirportNameFromFlightId(flightId);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        return airportRepository.bookATicket(flightId, passengerId);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        return airportRepository.cancelATicket(flightId, passengerId);
    }

    public int calculateFlightFare(Integer flightId) {
        return airportRepository.calculateFlightFare(flightId);
    }

    public int countPassengerBookings(Integer passengerId) {
       return airportRepository.countPassengerBookings(passengerId);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return airportRepository.calculateRevenueOfAFlight(flightId);
    }
}
