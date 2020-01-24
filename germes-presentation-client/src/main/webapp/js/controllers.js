app.controller('CityCtrl', ['$scope', 'cityService', function($scope, cityService) {
        $scope.rowsPerPage = 10;
        $scope.cities = cityService.query();
        $scope.isRegionCenter = function(city) {
            if(city.district) {
                return false;
            }
            return true;
        };
    }
]);

app.controller('TranslateCtrl', ['$translate', '$scope', function($translate, $scope) {
        $scope.changeLanguage = function(lang) {
            $translate.use(lang);
        };
}]);

app.controller('EventCtrl', ['$scope', function($scope) {
        $scope.currentTime = '';

        if (typeof(EventSource) !== "undefined") {
            var source = new EventSource('/api/sse/time');

            source.onmessage = function (event) {
                $scope.currentTime = event.data;
                $scope.$apply();
            };
        }
        else {
            console.log('SSE not supported by browser.');
        }
}]);