var map;
var messagewindow;
var jsonString = document.getElementById("imageLoading:Json").value;
var jsonObject = JSON.parse(jsonString);
var refresh
function resetRender() {
	document.getElementById("testje:render").click();
}
function getId() {
	var id = document.getElementById("imageId").innerHTML;
	document.getElementById("imageLoading:idPass").value = id;
	document.getElementById("imageLoading:passId").click();
}
function refresh() {
	refresh = document.getElementById("imageLoading:refresh").value;
	if (refresh == 0) {
		document.getElementById("imageLoading:dummy").click();
		refresh = refresh + 1;
	}
}
function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center : {
			lat : 51.05879387,
			lng : 3.712971
		},
		zoom : 5
	});
	infowindow = new google.maps.InfoWindow()
	var marker, i
	for (i in jsonObject) {
		marker = new google.maps.Marker({
			position : {
				lat : jsonObject[i]['lat'],
				lng : jsonObject[i]['lng']
			},
			map : map,
			title : jsonObject[i]['adres'] + ' '
					+ jsonObject[i]['project titel']
		});
		var content = '<div>'
				+ '<div id="imageId" style="display: none;">'
				+ jsonObject[i]['image id']
				+ '</div>'
				+ jsonObject[i]['naam']
				+ ' '
				+ jsonObject[i]['adres']
				+ ' '
				+ jsonObject[i]['project titel']
				+ ' '
				+ jsonObject[i]['eigenaar']
				+ '<h:panelGroup id="image" layout="block" rendered="true"><p:graphicImage value="#{imageController.foto}"><f:param name="id" value="#{locationController.fotoId}" /></p:graphicImage></h:panelGroup>'
				+ '</div>';

		google.maps.event.addListener(marker, 'click', (function(marker,
				content, infowindow) {
			return function() {
				infowindow.setContent(content);
				infowindow.open(map, marker);
				getId();
				setTimeout(refresh(), 100);
			};

		})(marker, content, infowindow));
		google.maps.event.addListener(infowindow, 'closeclick', function() {
			resetRender();
		});
	}
}