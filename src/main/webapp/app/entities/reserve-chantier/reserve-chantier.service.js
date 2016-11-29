(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('ReserveChantier', ReserveChantier);

    ReserveChantier.$inject = ['$resource'];

    function ReserveChantier ($resource) {
        var resourceUrl =  'api/reserve-chantiers/:id';

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
