/*
 * Copyright 2013 Ivan Pedrazas
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */



function ResultsCtrl($scope, $http) {
    $scope.query = 'python';
    $scope.search = function(){
        var safe_url = "/search.php?query="+$scope.query+"";
        $http({method: 'GET', url: 'http://localhost:9200/simple/webpage/_search?size=20&q='+$scope.query+'&fields=file.title,url'}).
            success(function(data, status, headers, config) {
                console.log(data.hits.hits;
                $scope.results = data.hits.hits;
        });
    }
};

