function setDepartureAirport() {
    var airport = $("#selectAirport").find(":selected").val();
    window.location.replace("/departure?airportcode="+airport);
}

function setArrivalAirport() {
    var airport = $("#selectAirportArrivals").find(":selected").val();
    window.location.replace("/departure?airportcode="+airport);
}