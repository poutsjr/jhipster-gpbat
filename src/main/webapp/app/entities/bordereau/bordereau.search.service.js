(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .factory('BordereauSearch', BordereauSearch);

    BordereauSearch.$inject = ['$resource'];

    function BordereauSearch($resource) {
        var resourceUrl =  'api/_search/bordereaus/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
