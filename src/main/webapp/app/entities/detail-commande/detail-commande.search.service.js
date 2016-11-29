(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('DetailCommandeSearch', DetailCommandeSearch);

    DetailCommandeSearch.$inject = ['$resource'];

    function DetailCommandeSearch($resource) {
        var resourceUrl =  'api/_search/detail-commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
