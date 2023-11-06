window.onload = function() {
    let markers = new Set();
    let polylines = new Set();
    let cur_marker = null;
    let cur_polyline = null;
    let right_click_selected = null;
    let click_state = 'add_marker'
    var mapContainer = document.getElementById('map');


    var mapOption = {
        center: new kakao.maps.LatLng(37.551262, 126.939670),
        level: 3
    };


    // map object
    var map = new kakao.maps.Map(mapContainer, mapOption);


    // create vertex
    kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
        if(click_state != 'add_vertex'){
            return;
        }

        // latitude and lng of clicked position
        var latlng = mouseEvent.latLng;

        let marker = addVertex(-1, latlng, '');

        let xhr = new XMLHttpRequest();
        xhr.open('POST', 'mapper/set-vertex');
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = () => {
            if (xhr.status == 200) {
                document.getElementById("result_text").innerHTML = "Created Marker!";

                marker.id = xhr.response;
                document.getElementById("vid").value = marker.id;
            }
        }

        xhr.onerror = () => {
            markers.delete(marker);
        }

        let send_object = {
            vid: marker.id,
            vlat: marker.lat,
            vlng: marker.lng,
            vname: ''
        }

        let send_json = JSON.stringify(send_object);
        xhr.send(send_json);


    });


    // delete vertex
    document.getElementById("delete_vertex").addEventListener('click', () => {
        let send_object = {
            vid: cur_marker.id,
            vlat: cur_marker.lat,
            vlng: cur_marker.lng,
            vname: cur_marker.name
        }

        let send_json = JSON.stringify(send_object);

        let xhr = new XMLHttpRequest();
        xhr.open('POST', 'mapper/delete-vertex');
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = () => {
            if (xhr.status == 200) {
                console.log(xhr.response);
                console.log("cur_marker:", cur_marker);
                if (xhr.response == 'true') {
                    cur_marker.setMap(null);
                    markers.delete(cur_marker);
                    cur_marker = null;
                }
                console.log(markers);
                console.log(cur_marker);
            }
        }

        xhr.send(send_json);

    })



    // If a vertex is selected, submit vertex data on click
    let form = document.getElementById('vertex_form');
    form.onsubmit = () => {
        let formData = new FormData(form);
        let xhr = new XMLHttpRequest();
        xhr.open('POST', 'mapper/set-vertex');
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onload = () => {
            if (xhr.status == 200) {
                document.getElementById("result_text").innerHTML = "set vertex success!";

            }
        }
        cur_marker['name'] = formData.get('vname');
        let send_object = JSON.stringify(Object.fromEntries(formData));
        xhr.send(send_object);

        return false;
    }


    // Edge related





    // Go to input latitude and longitude
    let bt_go = document.getElementById("go_lat_lng");
    bt_go.addEventListener('click', () => {
        let lat = document.getElementById("lat").value;
        let lng = document.getElementById("lng").value;
        map.setCenter(new kakao.maps.LatLng(Number(lat), Number(lng)));
    })










    // Initialize vertex and edge
    getAllVertex();



    function getAllVertex() {
        let xhr = new XMLHttpRequest();
        xhr.open('GET', 'mapper/get-all-vertex');
        xhr.onload = () => {
            if (xhr.status == 200) {
                let response = JSON.parse(xhr.response);
                console.log(response);
                Object.keys(response).forEach(key => {
                    let value = response[key];
                    let position = new kakao.maps.LatLng(value.vlat, value.vlng);

                    let marker = addVertex(value.vid, position, value.vname);
                })
            }
        }

        xhr.send();
    }


    // add vertex to markers, and handle marker events
    let addVertex = (id, latlng, name) => {

        var marker = new kakao.maps.Marker({
            position: latlng,
            clickable: true,
        });

        marker.id = id;
        marker.lat = latlng.getLat();
        marker.lng = latlng.getLng();
        marker.name = name;

        marker.setMap(map);
        markers.add(marker);


        // callback for normal click -> select
        let clickCallback = () => {
            if(click_state != 'select_vertex'){
                return;
            }
            cur_marker = marker;
            document.getElementById("vid").value = marker.id;
            document.getElementById("vlat").value = marker.lat;
            document.getElementById("vlng").value = marker.lng;
            document.getElementById("vname").value = marker.name;
        }

        kakao.maps.event.addListener(marker, 'click', clickCallback);
        clickCallback();

        // callback for right click -> select for edge
        let rightClickCallback = () => {
            if (right_click_selected == null) {
                right_click_selected = marker;
            }
            else {
                console.log(right_click_selected, marker);
                let line = addEdge(-1, right_click_selected, marker, 0, 0, 0, 0);
                let xhr = new XMLHttpRequest();

                xhr.open('POST', 'mapper/set-edge')
                xhr.setRequestHeader('Content-Type', 'application/json');

                xhr.onload = () => {
                    if (xhr.status == 200) {
                        line.id = xhr.response;
                        document.getElementById('eid').value = line.id;
                    }
                }

                let send_object = {
                    eid: line.id,
                    vstartId: line.vstart.id,
                    vendId: line.vend.id,
                    distance: line.distance,
                    slope: line.slope,
                    width: line.width,
                    population: line.population,
                    score: line.score
                }
                console.log(send_object);

                let send_json = JSON.stringify(send_object);

                xhr.send(send_json);
                right_click_selected = null;
            }
        }

        kakao.maps.event.addListener(marker, 'rightclick', rightClickCallback);



        // update latlng text
        var message = 'Lat: ' + latlng.getLat();
        message += 'Lng: ' + latlng.getLng();

        var resultDiv = document.getElementById('clickLatlng');
        resultDiv.innerHTML = message;


        return marker;
    }


    let addEdge = (id, vstart, vend, slope, width, population, score) => {

        let line = new kakao.maps.Polyline({
            map: map,
            path: [vstart.getPosition(), vend.getPosition()],
            strokeWeight: 7,
            strokeColor: '#db4040',
            strokeOpacity: 1,
            strokeStyle: 'solid'
        });

        let distance = line.getLength();

        line.id = id;
        line.vstart = vstart;
        line.vend = vend;
        line.distance = distance;
        line.slope = slope;
        line.width = width;
        line.population = population;
        line.score = score;

        kakao.maps.event.addListener(line, 'click', () => {
            if(click_state != 'select_edge'){
                return;
            }
            cur_polyline = line;

            document.getElementById('eid').value = line.id;
            document.getElementById('distance').value = line.distance;
            document.getElementById('slope').value = line.slope;
            document.getElementById('width').value = line.width;
            document.getElementById('population').value = line.population;
            document.getElementById('score').value = line.score;
        });

        polylines.add(line);

        return line;
    }







    // simple bt callbacks
    document.getElementById('add_vertex_bt').addEventListener('click', () => {
        click_state = 'add_vertex';
    })

    document.getElementById('select_vertex_bt').addEventListener('click', () => {
        click_state = 'select_vertex';
    })

    document.getElementById('select_edge_bt').addEventListener('click', () => {
        click_state = 'select_edge';
    })
}



