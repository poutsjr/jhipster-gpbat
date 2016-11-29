(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('AvancementChantier', AvancementChantier);

    AvancementChantier.$inject = ['$resource'];

    function AvancementChantier ($resource) {
        var resourceUrl =  'api/avancement-chantiers/:id';

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
