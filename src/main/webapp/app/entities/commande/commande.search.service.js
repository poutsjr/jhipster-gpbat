(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('CommandeSearch', CommandeSearch);

    CommandeSearch.$inject = ['$resource'];

    function CommandeSearch($resource) {
        var resourceUrl =  'api/_search/commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
