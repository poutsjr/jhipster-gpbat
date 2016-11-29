(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('CorpsEtat', CorpsEtat);

    CorpsEtat.$inject = ['$resource'];

    function CorpsEtat ($resource) {
        var resourceUrl =  'api/corps-etats/:id';

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
