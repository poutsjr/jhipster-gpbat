(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('BordereauDetailController', BordereauDetailController);

    BordereauDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bordereau', 'CorpsEtat'];

    function BordereauDetailController($scope, $rootScope, $stateParams, previousState, entity, Bordereau, CorpsEtat) {
        var vm = this;

        vm.bordereau = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:bordereauUpdate', function(event, result) {
            vm.bordereau = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
