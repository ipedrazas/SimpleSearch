



function ResultsCtrl($scope, $http) {
    $scope.query = 'python';
    $scope.search = function(){
        $http({method: 'GET', url: 'http://localhost:9200/simple/webpage/_search?size=10&q='+$scope.query+'&fields=webpage.url'}).
            success(function(data, status, headers, config) {
                console.log(data.hits.hits);
                $scope.results = data.hits.hits;
        });
    }
};

