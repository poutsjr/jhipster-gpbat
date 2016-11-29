(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('CorpsEtatSearch', CorpsEtatSearch);

    CorpsEtatSearch.$inject = ['$resource'];

    function CorpsEtatSearch($resource) {
        var resourceUrl =  'api/_search/corps-etats/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
