(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('PartenaireDetailController', PartenaireDetailController);

    PartenaireDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Partenaire'];

    function PartenaireDetailController($scope, $rootScope, $stateParams, previousState, entity, Partenaire) {
        var vm = this;

        vm.partenaire = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:partenaireUpdate', function(event, result) {
            vm.partenaire = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
