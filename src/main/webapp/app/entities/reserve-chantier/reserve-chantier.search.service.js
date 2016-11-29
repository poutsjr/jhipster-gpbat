(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('ReserveChantierSearch', ReserveChantierSearch);

    ReserveChantierSearch.$inject = ['$resource'];

    function ReserveChantierSearch($resource) {
        var resourceUrl =  'api/_search/reserve-chantiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
