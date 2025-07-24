import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Autore from './autore';
import AutoreDetail from './autore-detail';
import AutoreUpdate from './autore-update';
import AutoreDeleteDialog from './autore-delete-dialog';

const AutoreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Autore />} />
    <Route path="new" element={<AutoreUpdate />} />
    <Route path=":id">
      <Route index element={<AutoreDetail />} />
      <Route path="edit" element={<AutoreUpdate />} />
      <Route path="delete" element={<AutoreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AutoreRoutes;
