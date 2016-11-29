(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('ReceptionChantierSearch', ReceptionChantierSearch);

    ReceptionChantierSearch.$inject = ['$resource'];

    function ReceptionChantierSearch($resource) {
        var resourceUrl =  'api/_search/reception-chantiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
