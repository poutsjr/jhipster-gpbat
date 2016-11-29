(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('BordereauDialogController', BordereauDialogController);

    BordereauDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bordereau', 'CorpsEtat'];

    function BordereauDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bordereau, CorpsEtat) {
        var vm = this;

        vm.bordereau = entity;
        vm.clear = clear;
        vm.save = save;
        vm.corpsetats = CorpsEtat.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bordereau.id !== null) {
                Bordereau.update(vm.bordereau, onSaveSuccess, onSaveError);
            } else {
                Bordereau.save(vm.bordereau, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:bordereauUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
