(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('Bordereau', Bordereau);

    Bordereau.$inject = ['$resource'];

    function Bordereau ($resource) {
        var resourceUrl =  'api/bordereaus/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
