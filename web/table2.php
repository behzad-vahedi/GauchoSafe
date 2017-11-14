
<html>
<head>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GauchoSafe Location Report</title>
    <meta name="description" content="GauchoSafe is a platform for UCSB students and Isla Vista ">

    <link rel="stylesheet" href="css/main.css">
    <!-- <link rel="stylesheet" href="css/table.css"> -->
    <link rel="stylesheet" href="css/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"> 

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script> 

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.2.0/dist/leaflet.css" integrity="sha512-M2wvCLH6DSRazYeZRIm1JnYyh22purTM+FDB5CsyxtQJYeKq83arPe5wgbNmcFXGqiSH2XR8dT/fJISVA1r/zQ==" crossorigin=""/>
    <script src="https://unpkg.com/leaflet@1.2.0/dist/leaflet.js" integrity="sha512-lInM/apFSqyy1o6s89K4iQUKg6ppXEgsVxT35HbzUupEVRh2Eu9Wdl4tHj7dZO0s1uvplcYGmt3498TtHq+log==" crossorigin=""></script>


</head>
<body font-family: sans-serif text="black">

<!-- Navbar -->

<div class="w3-top">
  <div class="w3-bar w3-yellow w3-card-2">
    <a class="w3-bar-item w3-button w3-padding-large w3-hide-medium w3-hide-large w3-right" href="javascript:void(0)" onclick="myFunction()" title="Toggle Navigation Menu"><i class="fa fa-bars"></i></a>
    <a href="main.php" class="w3-bar-item w3-button w3-padding-large">HOME</a>
    <a href="table.php" class="w3-bar-item w3-button w3-padding-large w3-hide-small">LOCATION REPORTS</a>
    <a href="about.html" class="w3-bar-item w3-button w3-padding-large w3-hide-small">ABOUT US</a>
    <a href="contact.hml" class="w3-bar-item w3-button w3-padding-large w3-hide-small">CONTACT</a>
    
  </div>
</div>


<div class="w3-blue behzad-header">
  <img src="images/logo.png" align="right">
  <p class="w3-xlarge w3-center" >
  User Location Reports
</p>
</div>

<!-- Navbar on small screens -->
<div id="navDemo" class="w3-bar-block w3-black w3-hide w3-hide-large w3-hide-medium w3-top" style="margin-top:46px">
  <a href="main.php" class="w3-bar-item w3-button w3-padding-large">HOME</a>
  <a href="table.php" class="w3-bar-item w3-button w3-padding-large">LOCATION REPORTS</a>
  <a href="about.html" class="w3-bar-item w3-button w3-padding-large">ABOUT US</a>
  <a href="contact.hml" class="w3-bar-item w3-button w3-padding-large">CONTACT</a>
</div>


<!-- <div class="menu w3-yellow behzad-login-pane">
    
</div> -->


<!-- php code to get data from database -->
<?php require('config.php');

    //Create connection
    $conn = new mysqli(DBHOST,DBUSER,DBPASS,DBNAME);

    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $sql = "SELECT * FROM table_locs";
    $result = $conn->query($sql);

    $locationArray = array(array());

    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            $temp = array(array($row["latitude"], $row["longitude"]));
            $locationArray = array_merge($locationArray, $temp);
        }
    } else {
        echo "0 results";
    }

    // $sqlEms = "SELECT * FROM table_locs_ems";
    // $resultEms = $conn->query($sqlEms);

    // $locationArrayEms = array(array());

    // if ($resultEms->num_rows > 0) {
    //     // output data of each row
    //     while($row = $resultEms->fetch_assoc()) {
    //         $temp = array(array($row["latitude"], $row["longitude"]));
    //         $locationArray = array_merge($locationArray, $temp);
    //     }
    // } else {
    //     echo "0 results";
    // }

    $conn->close();
?>


<!-- <div class="behzad-map-pane main">  box-sizing: border-box; -->


 <!--  <div id="main-div" style=" box-sizing: border-box; top: 15%">
    <div style="width: 40%; background-color: red; float: left;">
      something here kjwnfeknkwef okwemfwef 
    </div>
    <div style="width: 60%; background-color: red; float: left">
      something else here  kewjfwejif qwuhqwd  ybcduysjf e 2jd f
    </div>
  </div> -->

  <div class="table behzad-login-pane">
    <!-- <div id="main-div" style="float: left; box-sizing: border-box;"> -->

<!-- <div style="width: 50%; height: 100%;"> -->
  <div class="wrap">
        <?php 

          echo '<table class="head" align="center">';
          echo '<tr><td "style="width: 20%;">ID</td><td style="width: 20%;">Email</td><td style="width: 32%;">Phone Number</td><td style="width: 20%;">Latitude</td><td style="width: 20%;">Longitude</td></tr>';
          echo '</table>';

          echo '<div class="inner_table">';
          echo '<table>';

          //Create connection
          $conn = new mysqli(DBHOST,DBUSER,DBPASS,DBNAME);

          // Check connection
          if ($conn->connect_error) {
              die("Connection failed: " . $conn->connect_error);
          }



          $sql = "SELECT * FROM table_locs_ems";
          $result = $conn->query($sql);

          if ($result->num_rows > 0) {
              // output data of each row
              while($row = $result->fetch_assoc()) {
                  $lat = number_format($row["latitude"],7);
                  $lon = number_format($row["longitude"],7);
                  echo "<tr><td style='width: 10%;'>{$row["id"]}</td><td style='width: 30%;'>{$row["email"]}</td><td style='width: 20%;'>{$row["phoneNumber"]}</td><td style='width: 20%;'>{$lat}</td><td style='width: 20%;'>{$lon}</td></tr>";

              }
          } else {
              echo "0 results";
          }


          echo '</table>';
          echo '</div>';

           $conn->close();

          // create trigger after inserting data to loc_ems
      ?>
  </div>
<!-- </div> -->
</div> 


  <!-- <div class="table-map"> -->
    <div class="table-map behzad-map-pane">
      <div id="mapid" style="width: 100%; height: 100%" align="middle">
          <script>
              var locations = <?php echo json_encode($locationArray); ?>;
              
              locations = locations.map(function (p) { return [parseFloat(p[0]), parseFloat(p[1])];});
              locations.splice(0,1);

              var mymap = L.map('mapid');//.setView([34.413,-119.858], 15);
              
              L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}', {
                attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
                maxZoom: 18,
                id: 'mapbox.streets',
                accessToken: 'pk.eyJ1IjoiYmVoemFkNjkiLCJhIjoiY2o0dWdkMXNjMGlqYzJxbmtybjhpeDIwaiJ9.uZHldoajwXmg-Fus9ViAsw'
            }).addTo(mymap);



              var popup = L.popup();

              function onMapClick(e) {
                  popup
                      .setLatLng(e.latlng)
                      .setContent("You clicked the map at " + e.latlng.toString())
                      .openOn(mymap);
              }

              mymap.on('click', onMapClick);


              var bounds = L.latLngBounds(locations);
              mymap.fitBounds(bounds);
              mymap.setZoom(10);
              // var heat = L.heatLayer(locations).addTo(mymap);
                  for (var i = 0; i < locations.length; i++) {
                    marker = new L.marker([locations[i][0],locations[i][1]])
                      .addTo(mymap);
                    // document.write(locations[i][1],locations[i][0]);
                  }
          </script>
      </div>
  </div>
<!-- </div> -->




    


<!-- <footer class="w3-container w3-padding-64 w3-center w3-light-grey w3-xlarge"> -->
<div class="behzad-footer" align="middle">
    <p class="w3-small">&copy; 2017 Behzad Vahedi</a></p>
</div>



</body>
</html>