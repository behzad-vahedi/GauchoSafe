
<html>
<head>
    <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to GauchoSafe</title>
    <meta name="description" content="GauchoSafe is a platform for UCSB students and Isla Vista ">

    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="css/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"> 

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script> 

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.2.0/dist/leaflet.css" integrity="sha512-M2wvCLH6DSRazYeZRIm1JnYyh22purTM+FDB5CsyxtQJYeKq83arPe5wgbNmcFXGqiSH2XR8dT/fJISVA1r/zQ==" crossorigin=""/>
    <script src="https://unpkg.com/leaflet@1.2.0/dist/leaflet.js" integrity="sha512-lInM/apFSqyy1o6s89K4iQUKg6ppXEgsVxT35HbzUupEVRh2Eu9Wdl4tHj7dZO0s1uvplcYGmt3498TtHq+log==" crossorigin=""></script>
    <script src="leaflet-heat.js"></script>


</head>
<body font-family: sans-serif>

<div id="id01" class="modal">

    <a href="javascript:closeFrame();">[X]</a>

    <iframe src="http://localhost/apiloc/web/loginregister-master/" width="800" height="600" align="middle">
        <!-- <a href="javascript:closeFrame();">[X]</a> -->
        <!-- <span onclick="closeFrame()">x</span> -->
    </iframe>

        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
  
   <!-- <form class="modal-content animate" action="/loginregister-master/index.php"> -->
    <!-- <div class="imgcontainer"> -->

      <!-- <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span> -->
      <!-- <span id='close' onclick="document.getElementById('id01').style.display='none'">x</span> -->

      <!-- <span id='close' onclick='this.parentNode.parentNode.parentNode.removeChild(this.parentNode.parentNode); return false;'>x</span> -->
      <!-- <img src="images/logo.png" alt="Avatar" class="avatar"> -->

      <!-- <span id='close'>x</span> -->
      <!-- <iframe src="http://localhost/apiloc/web/loginregister-master/" width="800" height="600" align="middle"></iframe>
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button> -->
    
    <!-- </div> -->
    <!-- <div class="container">
      <label><b>Username</b></label>
      <input type="text" placeholder="Enter Username" name="uname" required>

      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="psw" required>
        
      <button type="submit">Login</button>
      <input type="checkbox" checked="checked"> Remember me
    </div> -->

   <!--  <div  style="background-color:#f1f1f1">
      <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button> -->
      <!-- <span class="psw">Forgot <a href="#">password?</a></span> -->
    <!-- </div> -->
  <!-- </form>  -->


</div>

<script>
// Get the modal
    var modal = document.getElementById('id01');

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    function closeFrame() {
        document.getElementById("id01").style.display="none";
    }

</script>


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

<!-- style="max-width:100px;max-height:100px" align="right" -->

<div class="w3-blue behzad-header">
  <img src="images/logo.png" align="right">
  <p class="w3-xlarge w3-center" >
  Welcome to GauchoSafe
</p>
</div>

<!-- Navbar on small screens -->
<div id="navDemo" class="w3-bar-block w3-black w3-hide w3-hide-large w3-hide-medium w3-top" style="margin-top:46px">
  <a href="main.php" class="w3-bar-item w3-button w3-padding-large">HOME</a>
  <a href="table.php" class="w3-bar-item w3-button w3-padding-large">LOCATION REPORTS</a>
  <a href="about.html" class="w3-bar-item w3-button w3-padding-large">ABOUT US</a>
  <a href="contact.hml" class="w3-bar-item w3-button w3-padding-large">CONTACT</a>
</div>


<div class="menu w3-yellow behzad-login-pane">
    <p class="w3-medium">If you want to see a report of the location information submitted by GauchoSafe Android app users, please register.
    Login if you have already registered.</p>

    <button class="w3-button w3-blue w3-margin-bottom" style="margin-top:46px" onclick="document.getElementById('id01').style.display='block'">Show Location Reports</button>

</div>


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


<div class="main behzad-map-pane">
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
            var heat = L.heatLayer(locations).addTo(mymap);

        </script>
    </div>
</div>


<!-- <footer class="w3-container w3-padding-64 w3-center w3-light-grey w3-xlarge"> -->
<div class="behzad-footer" align="middle">
    <p class="w3-small">&copy; 2017 Behzad Vahedi</a></p>
</div>



</body>
</html>