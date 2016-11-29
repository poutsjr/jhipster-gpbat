(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('ChantierSearch', ChantierSearch);

    ChantierSearch.$inject = ['$resource'];

    function ChantierSearch($resource) {
        var resourceUrl =  'api/_search/chantiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
