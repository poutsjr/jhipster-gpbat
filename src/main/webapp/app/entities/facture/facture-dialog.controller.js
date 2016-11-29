(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('FactureDialogController', FactureDialogController);

    FactureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Facture'];

    function FactureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Facture) {
        var vm = this;

        vm.facture = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.facture.id !== null) {
                Facture.update(vm.facture, onSaveSuccess, onSaveError);
            } else {
                Facture.save(vm.facture, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:factureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
